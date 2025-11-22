package lk.jiat.smarttrade.service;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.core.Context;
import lk.jiat.smarttrade.dto.ProductDTO;
import lk.jiat.smarttrade.entity.*;
import lk.jiat.smarttrade.util.AppUtil;
import lk.jiat.smarttrade.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ProductService {

    public String updateProduct(Product product) {
        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";
        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = hibernateSession.beginTransaction();
        try {
            hibernateSession.merge(product);
            transaction.commit();
            status = true;
            message = "product images successfully updated";

        } catch (HibernateException e) {
            transaction.rollback();
            message = "product images could not be updated";
        } finally {
            hibernateSession.close();
        }

        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        return AppUtil.GSON.toJson(responseObject);
    }

    public Product getProductById(int id) {
        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
        Product product = hibernateSession.find(Product.class, id);
        hibernateSession.close();

        return product;
    }

    public String addNewProduct(ProductDTO productDTO, @Context HttpServletRequest request) {
        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";

        // PRODUCT INSERTING CODE
        if (productDTO.getBrandId() == 0) {
            message = "invalid brand. select a correct brand";
        } else if (productDTO.getModelId() == 0) {
            message = "invalid model type. select a correct model";
        } else if (productDTO.getTitle() == null) {
            message = "product title is required";
        } else if (productDTO.getTitle().isEmpty()) {
            message = "product title is empty";
        } else if (productDTO.getDescription() == null) {
            message = "product description is required";
        } else if (productDTO.getDescription().isEmpty()) {
            message = "product description is empty";
        } else if (productDTO.getStorageId() <= 0) {
            message = "invalid storage type.. select a correct storage";
        } else if (productDTO.getColorId() <= 0) {
            message = "invalid color. select a correct color";
        } else if (productDTO.getQualityId() <= 0) {
            message = "invalid condition type. select a correct condition";
        } else if (productDTO.getPrice() <= 0) {
            message = "product price cannot be less than or equal to 0";
        } else if (productDTO.getQty() <= 0) {
            message = "product quantity cannot be less than or equal to 0";
        } else {
            HttpSession httpSession = request.getSession(false);
            if (httpSession == null) {
                message = "session expired. please login";
            } else if (httpSession.getAttribute("user") == null) {
                message = "user not logged in";
            } else {
                User sessionUser = (User) httpSession.getAttribute("user");
                Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
                Seller seller = hibernateSession.createQuery("FROM Seller s WHERE s.user=:user", Seller.class)
                        .setParameter("user", sessionUser)
                        .getSingleResultOrNull();

                if (seller == null) {
                    message = "The requested profile is not a seller account. please register first as a seller";
                } else {
                    if (!seller.getStatus().getValue().equals(String.valueOf(Status.Type.APPROVED))) {
                        message = "account not approved.. please be patient till admin approved";
                    } else {
                        Model model = hibernateSession.find(Model.class, productDTO.getModelId());
                        if (model == null) {
                            message = "model not found. please contact administration";
                        } else {
                            Storage storage = hibernateSession.find(Storage.class, productDTO.getStorageId());
                            if (storage == null) {
                                message = "storage not found. please contact administration";
                            } else {
                                Color color = hibernateSession.find(Color.class, productDTO.getColorId());
                                if (color == null) {
                                    message = "color not found. please contact administration";
                                } else {
                                    Quality quality = hibernateSession.find(Quality.class, productDTO.getQualityId());
                                    if (quality == null) {
                                        message = "quality not found. please contact administration";
                                    } else {
                                        Product product = new Product();
                                        product.setTitle(productDTO.getTitle());
                                        product.setDescription(productDTO.getDescription());
                                        product.setModel(model);
                                        product.setStorage(storage);
                                        product.setQuality(quality);
                                        product.setColor(color);
                                        product.setSeller(seller);

                                        Stock stock = new Stock();
                                        stock.setProduct(product);
                                        stock.setPrice(productDTO.getPrice());
                                        stock.setQty(productDTO.getQty());

                                        Discount deafultDiscount = hibernateSession.createNamedQuery("Discount.findDefault", Discount.class)
                                                .getSingleResult();

                                        Status pendingStatus = hibernateSession.createNamedQuery("Status.findByValue", Status.class)
                                                .setParameter("value", String.valueOf(Status.Type.PENDING))
                                                .getSingleResult();
                                        stock.setDiscount(deafultDiscount);
                                        stock.setStatus(pendingStatus);

                                        Transaction transaction = hibernateSession.beginTransaction();
                                        try {
                                            hibernateSession.persist(product);
                                            hibernateSession.persist(stock);
                                            transaction.commit();
                                            status = true;
                                            responseObject.addProperty("productId", product.getId());
                                        } catch (HibernateException e) {
                                            transaction.rollback();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                hibernateSession.close();
            }
        }
        // PRODUCT INSERTING CODE

        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        return AppUtil.GSON.toJson(responseObject);
    }
}
