package com.damian;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class ContactController {
	private SessionFactory sessionFactory;
	
	public ContactController() {
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		MetadataSources sources = new MetadataSources(registry);
		sessionFactory = sources.buildMetadata().buildSessionFactory();
	}
	
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public String createContact(@RequestParam("name") String name,
								@RequestParam("email") String email,
								@RequestParam("phone") String phone,
								Model model) {
		Contact contact = new Contact(name, email, phone);
		
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(contact);
		session.getTransaction().commit();
		session.close();
		
		model.addAttribute("message", "Contact created successfully.");
		return "create";
		
	}
	@RequestMapping(value="/read", method=RequestMethod.GET)
	public String readContact(@RequestParam("id") int id, Model model) {
		Session session = sessionFactory.openSession();
		Contact contact =(Contact) session.get(Contact.class, id);
		session.close();
		
		model.addAttribute("contact", contact);
		return "read";
		
	}
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public String updateContact(@RequestParam("id") int id,
								@RequestParam("name") String name,
								@RequestParam("emial") String email,
								@RequestParam("phone") String phone,
								Model model) {
		Session session = sessionFactory.openSession();
		Contact contact = (Contact) session.get(Contact.class, id);
		if (contact != null) {
			contact.setName(name);
			contact.setEmail(email);
			contact.setPhone(phone);
			
			session.beginTransaction();
			session.update(contact);
			session.getTransaction().commit();
			
			model.addAttribute("messege", "Contact updated successfully.");
		} else {
			model.addAttribute("message", "Contact not found.");
			
		}
		session.close();
		return "update";
	}
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String deleteContact(@RequestParam("id") int id, Model model) {
		Session session = sessionFactory.openSession();
		Contact contact = (Contact) session.get(Contact.class, id);
		if (contact != null) {
			session.beginTransaction();
			session.delete(contact);
			session.getTransaction().commit();
			
			model.addAttribute("message", "Contact deleted successfully.");
		} else {
			model.addAttribute("message", "Contact not found.");
		}
		session.close();
		return "delete";
	}																			
}
