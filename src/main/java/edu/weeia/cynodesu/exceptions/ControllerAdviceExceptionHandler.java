package edu.weeia.cynodesu.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerAdviceExceptionHandler extends ResponseEntityExceptionHandler {



    //TODO: FIGURE OUT HOW TO CUSTOMIZE LOGIC DEPENDING ON BUSINESS SCENARIO.
    /***
     *
     * @param ex is an exception lol.
     * @param model is state machine for front view.
     * @return You want to return User-Friendly fragments of views where an actual error was created from with a message.
     *
     * Ideally you want an UI update when such error occurs.
     */
    @ExceptionHandler({DogCreationException.class, DogCreationException.class, DogNullException.class, DogImageUploadException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequestException(Exception ex, Model model) {
        logger.error("Bad request error occurred!", ex); // Logs the stack trace
        model.addAttribute("message", "An unexpected error occurred. " + ex.getMessage());
        return "error/400";
    }
//
//    @ExceptionHandler({ResourceNotFoundException.class})
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public String handleNoAccessException(Exception ex, Model model) {
//        model.addAttribute("message", "An unexpected error occurred." + ex.getMessage());
//        return "error/403";S
//    }

    @ExceptionHandler({ResourceNotFoundException.class, FacilityImageUploadException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(Exception ex, Model model) {
        model.addAttribute("message", "An unexpected error occurred." + ex.getMessage());
        logger.error("Resource not found error occurred! ", ex); // Logs the stack trace
        return "error/404";
    }

//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public String handleGeneralException(Exception ex, Model model) {
//        logger.error("An unexpected error occurred", ex); // Logs the stack trace
//        model.addAttribute("message", "An unexpected error occurred. " + ex.getMessage());
//        return "error/500";
//    }
}
