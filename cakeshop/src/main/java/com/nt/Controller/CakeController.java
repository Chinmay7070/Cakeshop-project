package com.nt.Controller;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.nt.CakeService.CartDetail;
import com.nt.CakeService.EmailService;
import com.nt.CakeService.ICakeService;
import com.nt.CakeService.ICartService;
import com.nt.CakeService.IOderService;
import com.nt.CakeService.PdfService;
import com.nt.model.Cake;
import com.nt.model.Cart;
import com.nt.model.Order;

@Controller
public class CakeController {
	
	@Autowired
	private ICartService caservice;

	@Autowired
	private ICakeService cservice;
	
	@Autowired
	private IOderService oservice;
	
	@Autowired
	private PdfService pservice;
	
	@Autowired
	private EmailService eservice;
	
	@GetMapping("/cakes")
	public String getAllCakes(Model model) {
		
		List<Cake> cakes = cservice.getAllCakes();
		model.addAttribute("cakes",cakes);
		return "cakes";
	}
	
	@PostMapping("/buy")
    public String buyCake(@ModelAttribute Order order, Model model) {
    
        Cake cake = cservice.getCakeById(order.getCakeId());
        if (cake == null) {
            model.addAttribute("error", "Cake not found!");
            return "error";
        }
        oservice.saveOrder(order);
        
        byte[] pdfBytes = pservice.generateBillPdf(cake, order.getCustomerEmail());
        
        try {
            eservice.sendBillEmail(order.getCustomerEmail(), pdfBytes);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to send bill email!");
            return "error";
        }

        model.addAttribute("message", "Order placed!");
        return "success";
	}
	
	
	 @GetMapping("/admin")
	    public String getAdminPage(Model model) {
	        System.out.println("Accessing /admin page");
	        List orders = oservice.getAllOrders();
	        List cakes = cservice.getAllCakes();
	        model.addAttribute("orders", orders);
	        model.addAttribute("cakes", cakes);
	        model.addAttribute("cake", new Cake()); // For form binding
	        return "admin";
	    }

	    @PostMapping("/add-cake")
	    public String addCake(
	            @ModelAttribute("cake") Cake cake,
	            @RequestParam("image") MultipartFile image,
	            Model model) {
	        try {
	            // Log request received
	            System.out.println("Received POST request for /add-cake");
	            System.out.println("Cake details: name=" + cake.getName() + ", price=" + cake.getPrice() + ", description=" + cake.getDescription());

	            // Validate inputs
	            if (cake.getName() == null || cake.getName().trim().isEmpty()) {
	                model.addAttribute("message", "Cake name cannot be empty!");
	                List orders = oservice.getAllOrders();
	                List cakes = cservice.getAllCakes();
	                model.addAttribute("orders", orders);
	                model.addAttribute("cakes", cakes);
	                model.addAttribute("cake", new Cake());
	                return "admin";
	            }
	            if (cake.getPrice() <= 0) {
	                model.addAttribute("message", "Price must be greater than zero!");
	                List orders = oservice.getAllOrders();
	                List cakes = cservice.getAllCakes();
	                model.addAttribute("orders", orders);
	                model.addAttribute("cakes", cakes);
	                model.addAttribute("cake", new Cake());
	                return "admin";
	            }
	            if (image.isEmpty()) {
	                model.addAttribute("message", "Please select an image file!");
	                List orders = oservice.getAllOrders();
	                List cakes = cservice.getAllCakes();
	                model.addAttribute("orders", orders);
	                model.addAttribute("cakes", cakes);
	                model.addAttribute("cake", new Cake());
	                return "admin";
	            }

	            // Save image to uploads/images using absolute path
	            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
	            String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/uploads/images/";
	            File uploadFolder = new File(uploadDir);
	            if (!uploadFolder.exists()) {
	                uploadFolder.mkdirs();
	                System.out.println("Created directory: " + uploadDir);
	            }
	            File file = new File(uploadDir + fileName);
	            image.transferTo(file);
	            System.out.println("Image saved to: " + file.getAbsolutePath());

	            // Set image path and save cake to database
	            cake.setImage_path("/uploads/images/" + fileName);
	            cservice.saveCake(cake);
	            System.out.println("Cake saved to database: " + cake.getName());

	            model.addAttribute("message", "Cake added successfully!");
	        } catch (Exception e) {
	            System.out.println("Error in /add-cake: " + e.getMessage());
	            e.printStackTrace();
	            model.addAttribute("message", "Failed to add cake: " + e.getMessage());
	            List orders = oservice.getAllOrders();
	            List cakes = cservice.getAllCakes();
	            model.addAttribute("orders", orders);
	            model.addAttribute("cakes", cakes);
	            model.addAttribute("cake", new Cake());
	            return "admin";
	        }
	        return "redirect:/admin";
	    }
	    
	    @GetMapping("/delete-cake")
	    public String deleteCake(@RequestParam("id") Long id, Model model) {
	        try {
	            System.out.println("Received GET request for /delete-cake with id: " + id);
	            Cake cake = cservice.getCakeById(id);
	            if (cake == null) {
	                System.out.println("Cake with ID " + id + " not found");
	                model.addAttribute("message", "Cake with ID " + id + " not found!");
	                List orders = oservice.getAllOrders();
	                List cakes = cservice.getAllCakes();
	                model.addAttribute("orders", orders);
	                model.addAttribute("cakes", cakes);
	                model.addAttribute("cake", new Cake());
	                return "admin";
	            }

	            // Delete image file if it exists
	            String imagePath = cake.getImage_path();
	            if (imagePath != null && !imagePath.isEmpty()) {
	                String fullPath = System.getProperty("user.dir") + "/src/main/resources/static" + imagePath;
	                File imageFile = new File(fullPath);
	                if (imageFile.exists()) {
	                    imageFile.delete();
	                    System.out.println("Image deleted: " + fullPath);
	                } else {
	                    System.out.println("Image file not found: " + fullPath);
	                }
	            }

	            // Delete cake from database
	            cservice.deleteCake(id);
	            System.out.println("Cake deleted from database: ID=" + id);

	            model.addAttribute("message", "Cake deleted successfully!");
	        } catch (Exception e) {
	            System.out.println("Error in /delete-cake: " + e.getMessage());
	            e.printStackTrace();
	            model.addAttribute("message", "Failed to delete cake: " + e.getMessage());
	            List orders = oservice.getAllOrders();
	            List cakes = cservice.getAllCakes();
	            model.addAttribute("orders", orders);
	            model.addAttribute("cakes", cakes);
	            model.addAttribute("cake", new Cake());
	            return "admin";
	        }
	        return "redirect:/admin";
	    }
	    
	    @PostMapping("/add-to-cart")
	    public String addToCart(@RequestParam Long cakeId,Model model)
	    {
	    	Cart cart = new Cart();
	    	cart.setCakeId(cakeId);
	    	cart.setUserId(1L);
	    	cart.setQuantity(1);
	    	caservice.addToCart(cart);
	    	model.addAttribute("message","cake added to cart!");
	    	return "redirect:/cakes";
	    	
	    }
	    @GetMapping("/cart")
	    public String getCartPage(Model model) {
	        List cartDetails = caservice.getAllCartItemsWithDetails();
	        double totalPrice = 0;
	        for (int i = 0; i < cartDetails.size(); i++) {
	            CartDetail cartDetail = (CartDetail) cartDetails.get(i);
	            totalPrice = totalPrice + (cartDetail.getCakePrice() * cartDetail.getQuantity());
	        }
	        model.addAttribute("cartItems", cartDetails);
	        model.addAttribute("totalPrice", totalPrice);
	        return "cart";
	    }
	    
	    @PostMapping("/generate-bill")
		public String generateBill(
				@RequestParam("customerName") String customerName,
				@RequestParam("customerEmail") String customerEmail,
				@RequestParam("customerPhone") String customerPhone,
				@RequestParam("customerAddress") String customerAddress,
				Model model) {
			List cartDetails = caservice.getAllCartItemsWithDetails();
			if (cartDetails.isEmpty()) {
				model.addAttribute("message", "Cart is empty! Please add items to generate a bill.");
				model.addAttribute("cartItems", cartDetails);
				model.addAttribute("totalPrice", 0.0);
				return "cart";
			}

			// Save orders for each cart item
			for (int i = 0; i < cartDetails.size(); i++) {
				CartDetail cartDetail = (CartDetail) cartDetails.get(i);
				Order order = new Order();
				order.setCakeId(cartDetail.getCakeId());
				order.setCustomerName(customerName);
				order.setCustomerEmail(customerEmail);
				order.setCustomerPhone(customerPhone);
				order.setCustomerAddress(customerAddress);
				oservice.saveOrder(order);
			}

			// Calculate total price
			double totalPrice = 0;
			for (int i = 0; i < cartDetails.size(); i++) {
				CartDetail cartDetail = (CartDetail) cartDetails.get(i);
				totalPrice = totalPrice + (cartDetail.getCakePrice() * cartDetail.getQuantity());
			}

			// Generate PDF
			byte[] pdfBytes = pservice.generateCartBillPdf(cartDetails, customerName, customerEmail, customerPhone, customerAddress);

			// Send email
			try {
				eservice.sendBillEmail(customerEmail, pdfBytes);
			} catch (Exception e) {
				model.addAttribute("message", "Failed to send bill email!");
				model.addAttribute("cartItems", cartDetails);
				model.addAttribute("totalPrice", totalPrice);
				return "cart";
			}

			// Clear cart after successful order
			caservice.clearCart();

			model.addAttribute("message", "Payment Done! Order placed and bill sent to email! Total: ₹" + totalPrice);
			return "success";
		}
	   
 }

