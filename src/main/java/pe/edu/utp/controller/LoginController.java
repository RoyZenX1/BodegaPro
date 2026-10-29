package pe.edu.utp.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {
    @GetMapping("/")
    public String inicio(HttpSession session) {
        return session.getAttribute("usuario") != null ? "redirect:/dashboard" : "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String usuario, @RequestParam String password, HttpSession session, Model model) {
        if ("admin".equals(usuario) && "admin123".equals(password)) {
            session.setAttribute("usuario", "Admin");
            return "redirect:/dashboard";
        }
        model.addAttribute("error", "Usuario o contraseña incorrectos.");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
