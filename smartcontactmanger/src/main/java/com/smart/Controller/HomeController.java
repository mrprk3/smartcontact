package com.smart.Controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.helper.Message;
import com.smart.model.User;

@Controller
public class HomeController {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home - contact manager");
	return "home";
	}
	
	
	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About - contact manager");
	return "about";
	}
	
	
	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Register - contact manager");
		model.addAttribute("user",new User());
	return "signup";
	}
	
	
	//handler for register user
	@RequestMapping(value="/do_register", method=RequestMethod.POST)
	public String registerUser(@ModelAttribute("user") User user,@RequestParam(value="agreement",defaultValue = "false") boolean agreement,Model model, HttpSession session) {
		try {
			
			if(!agreement) {
				System.out.println("You have not agreed terms and condition");
				throw new Exception("You have not agreed terms and condition");
			}
			user.setRole("Role_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			
			System.out.println("Agreement"+agreement);
			System.out.println("USER"+user);
			User result = this.userRepository.save(user);
			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("succesfully registered", "alert-success"));
			return "signup";
			
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("sometging went wrong !!"+e.getMessage(), "alert-error"));
			return "signup";
		}
		
			
	}
	
}
