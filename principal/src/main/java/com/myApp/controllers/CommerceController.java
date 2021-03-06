package com.myApp.controllers;

import com.myApp.exception.ApplicationException;
import com.myApp.exception.ApplicationExceptionCause;
import com.myApp.order.builder.Product_OrderBuilder;
import com.myApp.order.dto.Product_OrderDto;
import com.myApp.order.model.Order;
import com.myApp.model.UserProfile;
import com.myApp.order.builder.OrderBuilder;
import com.myApp.order.builder.OrderDtoBuilder;
import com.myApp.order.dto.OrderDto;
import com.myApp.order.model.Product_Order;
import com.myApp.order.service.OrderServiceImpl;
import com.myApp.profile.builder.ProfileBuilder;
import com.myApp.profile.builder.ProfileDtoBuilder;
import com.myApp.profile.dto.ProfileDto;
import com.myApp.profile.service.ProfileService;
import com.myApp.security.service.UserAppService;
import com.myApp.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("/api")
public class CommerceController {

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private ProfileService profileService;

   @Autowired
   private UserAppService userAppService;

   @Autowired
   private Util util;

    @GetMapping(value = "/orders")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<Order> orders = orderService.getOrders();
        return new ResponseEntity<>(
                orders.stream().map(
                        order -> new OrderDtoBuilder(order).getOrderDto())
                        .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    @PostMapping(value = "/order")
    public ResponseEntity<OrderDto> saveOrder(@RequestHeader(value = "Authorization") String token, @RequestBody OrderDto orderDto) {
        long id = userAppService.getIdByToken(token);

        Order order = new OrderBuilder(orderDto).getOrder();
        Order _order = orderService.saveOrder(order, id);
        return new ResponseEntity<>(
                new OrderDtoBuilder(_order).getOrderDto(),
                HttpStatus.OK
        );
    }

    @PostMapping(value = "/profile")
    public ResponseEntity<ProfileDto> saveProfile(@RequestHeader(value = "Authorization") String token, @RequestBody ProfileDto profileDto) {
        util.checkId(profileDto.getId());

        if (userAppService.getIdByToken(token) == profileDto.getId()) {
            UserProfile profile = new ProfileBuilder(profileDto).getProfile();
            return new ResponseEntity<>(
                    new ProfileDtoBuilder(profileService.save(profile)).getProfileDto(),
                    HttpStatus.CREATED
            );
        } throw new ApplicationException(ApplicationExceptionCause.NOT_ALLOW);
    }

    @GetMapping(value = "/order/{id_user}")
    public ResponseEntity<List<OrderDto>> getOrdersOfClient(@RequestHeader(value = "Authorization") String token, @PathVariable long id_user) {
        util.checkId(id_user);

        if (userAppService.getIdByToken(token) == id_user) {
            final List<Order> orders = orderService.getOrdersOfClient(id_user);
            return new ResponseEntity<>(
                    orders.stream().map(
                            order -> new OrderDtoBuilder(order).getOrderDto())
                            .collect(Collectors.toList()),
                    HttpStatus.OK
            );
        } throw new ApplicationException(ApplicationExceptionCause.NOT_ALLOW);
    }

    @GetMapping(value = "/order/{id_user}/{id_order}")
    public ResponseEntity<OrderDto> getOrderByIdOfClient(@RequestHeader(value = "Authorization") String token, @PathVariable long id_user, @PathVariable long id_order) {
        util.checkId(id_user);
        util.checkId(id_order);

        if (userAppService.getIdByToken(token) == id_user) {
            Order order = orderService.getOrderByIdAndProfile(id_order, id_user);
            return new ResponseEntity<>(
                    new OrderDtoBuilder(order).getOrderDto(),
                    HttpStatus.OK
            );
        } throw new ApplicationException(ApplicationExceptionCause.NOT_ALLOW);
    }

    @DeleteMapping(value = "/deleteOrder/{id_order}")
    public ResponseEntity<Boolean> deleteOrderById(@RequestHeader(value = "Authorization") String token, @PathVariable long id_order) {
        util.checkId(id_order);

        Order order = orderService.getOrderById(id_order);
        if (userAppService.getIdByToken(token) == order.getProfile().getId()) {
            return new ResponseEntity<>(
                    orderService.deleteOrder(id_order),
                    HttpStatus.OK
            );
        } throw new ApplicationException(ApplicationExceptionCause.NOT_ALLOW);
    }

    @DeleteMapping(value = "/deleteProductOfOrder/{id_order}/{id_product_order}")
    public ResponseEntity<OrderDto> deleteProductOfOrder(@RequestHeader(value = "Authorization") String token, @PathVariable long id_order, @PathVariable long id_product_order) {
        util.checkId(id_order);
        util.checkId(id_product_order);

        Order order = orderService.getOrderById(id_order);
        if (userAppService.getIdByToken(token) == order.getProfile().getId()) {
            return new ResponseEntity<>(
                    new OrderDtoBuilder(orderService.deleteProduct_Order(id_order, id_product_order)).getOrderDto(),
                    HttpStatus.OK
            );
        } throw new ApplicationException(ApplicationExceptionCause.NOT_ALLOW);
    }

    @PutMapping(value = "/addProductToOrder/{id_order}")
    public ResponseEntity<OrderDto> addProductToOrder(@RequestHeader(value = "Authorization") String token, @PathVariable long id_order, @RequestBody Product_OrderDto product_orderDto) {
        util.checkId(id_order);

        Product_Order product_order = new Product_OrderBuilder(product_orderDto).getProduct_order();
        Order order = orderService.getOrderById(id_order);
        if (userAppService.getIdByToken(token) == order.getProfile().getId()) {
            return new ResponseEntity<>(
                    new OrderDtoBuilder(orderService.addProductToOrder(id_order, product_order)).getOrderDto(),
                    HttpStatus.OK
            );
        } throw new ApplicationException(ApplicationExceptionCause.NOT_ALLOW);
    }

    @PutMapping(value = "/updateProductOfOrder/{id_order}/{id_product_order}/{quantity}")
    public ResponseEntity<Order> addProductToOrder(@RequestHeader(value = "Authorization") String token, @PathVariable long id_order, @PathVariable long id_product_order, @PathVariable long quantity) {
        util.checkId(id_order);
        util.checkId(id_product_order);
        util.checkNumber(quantity);

        Order order = orderService.getOrderById(id_order);
        if (userAppService.getIdByToken(token) == order.getProfile().getId()) {
            return new ResponseEntity<>(
                    orderService.updateProductOfOrder(id_order, id_product_order, quantity),
                    HttpStatus.OK
            );
        } throw new ApplicationException(ApplicationExceptionCause.NOT_ALLOW);
    }

    @PutMapping(value = "/updateProfile")
    public ResponseEntity<ProfileDto> updateUser(@Valid @RequestBody ProfileDto profileDto) {
        UserProfile profile = new ProfileBuilder(profileDto).getProfile();
        final UserProfile _profile = profileService.updateProfile(profile);
        return new ResponseEntity<>(
                new ProfileDtoBuilder(_profile).getProfileDto(),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/profiles")
    public ResponseEntity<List<ProfileDto>> getAllProfiles() {
        List<UserProfile> profiles = profileService.getAllUsers();
        return new ResponseEntity<>(
                profiles.stream().map(
                        profile -> new ProfileDtoBuilder(profile).getProfileDto())
                        .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/profileClient")
    public ResponseEntity<ProfileDto> getProfileOfClient(@RequestHeader(value = "Authorization") String token) {
        long id = userAppService.getIdByToken(token);

        UserProfile profile = profileService.getUserProfileById(id);
        return new ResponseEntity<>(
                new ProfileDtoBuilder(profile).getProfileDto(),
                HttpStatus.OK
        );
    }
}
