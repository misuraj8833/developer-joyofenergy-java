package uk.tw.energy.energysuppliererrorcontroller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EnergySupplierErrorControllerTest {

    @InjectMocks
    EnergySupplierErrorController energySupplierErrorController;
    @Mock
    HttpServletRequest httpServletRequest;
    @Mock
    Model model;

    @Test
    void getErrorOccuredInSpringBootThrowTheCustomErrorPage()
    {
        String returnedPageName = energySupplierErrorController.handleError(httpServletRequest,model);
        assertEquals(returnedPageName,"error");
    }

}