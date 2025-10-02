package com.nt.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.nt.CakeService.EmailService;
import com.nt.CakeService.ICakeService;
import com.nt.CakeService.IOderService;
import com.nt.CakeService.PdfService;
import com.nt.model.Cake;
import com.nt.model.Order;

@Controller
public class CakeController {

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
        // Order मधून cakeId वापरून Cake मिळवा
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
	
}
