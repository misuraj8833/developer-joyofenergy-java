package uk.tw.energy.energysuppliererrorcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
public class EnergySupplierErrorController
{
    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model)
    {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null)
        {
            model.addAttribute("timestamp",new Date().getTime());
            return "error";
        }
        return "error";
    }

}
