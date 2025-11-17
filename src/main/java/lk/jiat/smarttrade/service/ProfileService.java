package lk.jiat.smarttrade.service;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Context;
import lk.jiat.smarttrade.dto.UserDTO;
import lk.jiat.smarttrade.util.AppUtil;
import lk.jiat.smarttrade.validation.Validator;

public class ProfileService {
    public String updateProfile(UserDTO userDTO, @Context HttpServletRequest request) {
        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";

        //PROFILE-UPDATE
        if (userDTO.getFirstName() == null) {
            message = "First name is required!";
        } else if (userDTO.getFirstName().isBlank()) {
            message = "First name can not be empty!";
        } else if (userDTO.getLastName() == null) {
            message = "Last name is required!";
        } else if (userDTO.getLastName().isBlank()) {
            message = "Last name can not be empty!";
        } else if (userDTO.getLineOne() == null) {
            message = "Address line one is required!";
        } else if (userDTO.getLineOne().isBlank()) {
            message = "Address line one can not be empty!";
        } else if (userDTO.getLineTwo() != null && userDTO.getLineTwo().isBlank()) {
            message = "Address line two can not be empty!";
        } else if (userDTO.getPostalCode() != null && !userDTO.getPostalCode().matches(Validator.POSTAL_CODE_VALIDATION)) {
            message = "Enter a valid postal code!";
        } else if (userDTO.getCityId() == 0) {
            message = "please select a city";
        } else if (userDTO.getPassword() == null) {
            message = "Password is required!";
        } else if (userDTO.getPassword().isBlank()) {
            message = "Password can not be empty!";
        } else if (!userDTO.getPassword().matches(Validator.PASSWORD_VALIDATION)) {
            message = "Please provide valid password. \n " +
                    "The password must be at least 8 characters long and include at least one uppercase letter, " +
                    "one lowercase letter, one digit, and one special character";
        } else if (userDTO.getNewPassword() != null && userDTO.getNewPassword().isBlank()) {
            message = "New Password is required!";
        } else if (userDTO.getNewPassword() != null && !userDTO.getNewPassword().matches(Validator.PASSWORD_VALIDATION)) {
            message = "Please provide valid password. \n " +
                    "The password must be at least 8 characters long and include at least one uppercase letter, " +
                    "one lowercase letter, one digit, and one special character";
        } else if (userDTO.getConfirmPassword() != null && userDTO.getConfirmPassword().isBlank()) {
            message = "Confirm Password is required!";
        } else if (userDTO.getConfirmPassword() != null && !userDTO.getConfirmPassword().matches(Validator.PASSWORD_VALIDATION)) {
            message = "Please provide valid password. \n " +
                    "The password must be at least 8 characters long and include at least one uppercase letter, " +
                    "one lowercase letter, one digit, and one special character";
        } else if (userDTO.getNewPassword() != null && userDTO.getConfirmPassword() != null) {
            if (!userDTO.getNewPassword().equals(userDTO.getConfirmPassword())) {
                message = "Passwords don't match!";
            }
        }else{

        }

        //PROFILE-UPDATE

        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        return AppUtil.GSON.toJson(responseObject);
    }
}
