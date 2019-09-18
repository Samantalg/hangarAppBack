package com.myApp.controllers;

import com.myApp.exception.ApplicationException;
import com.myApp.exception.ApplicationExceptionCause;
import com.myApp.order.model.Order;
import com.myApp.model.UserProfile;
import com.myApp.order.builder.OrderBuilder;
import com.myApp.order.builder.OrderDtoBuilder;
import com.myApp.order.dto.OrderDto;
import com.myApp.order.service.OrderService;
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
@RequestMapping("/api/commerce")
public class CommerceController {

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private ProfileService profileService;

   @Autowired
   private UserAppService userAppService;

   @Autowired
   private Util util;

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDto>> getAllOrders() throws Exception {
        List<Order> orders = orderService.getOrders();
        return new ResponseEntity<>(
                orders.stream().map(
                        order -> new OrderDtoBuilder(order).getOrderDto())
                        .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    @PostMapping("/order")
    public ResponseEntity<OrderDto> saveOrder(@RequestHeader(value = "Authorization") String token, @RequestBody OrderDto orderDto) throws Exception {
        if (userAppService.getIdByToken(token) == orderDto.getProfile().getId()) {
            Order order = new OrderBuilder(orderDto).getOrder();
            Order _order = orderService.saveOrder(order);
            return new ResponseEntity<>(
                    new OrderDtoBuilder(_order).getOrderDto(),
                    HttpStatus.OK
            );
        } throw new ApplicationException(ApplicationExceptionCause.NOT_ALLOW);
    }

    @PostMapping("/profile")
    public ResponseEntity<ProfileDto> saveProfile(@RequestHeader(value = "Authorization") String token, @RequestBody ProfileDto profileDto) throws Exception {
        util.checkId(profileDto.getId());

        if (userAppService.getIdByToken(token) == profileDto.getId()) {
            UserProfile profile = new ProfileBuilder(profileDto).getProfile();
            return new ResponseEntity<>(
                    new ProfileDtoBuilder(profileService.save(profile)).getProfileDto(),
                    HttpStatus.CREATED
            );
        } throw new ApplicationException(ApplicationExceptionCause.NOT_ALLOW);
    }

    @RequestMapping(value = "/order/{id_user}",  method = RequestMethod.GET)
    public ResponseEntity<List<OrderDto>> getOrdersOfClient(@RequestHeader(value = "Authorization") String token, @PathVariable long id_user) throws Exception {
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

    @RequestMapping(value = "/order/{id_user}/{id_order}",  method = RequestMethod.GET)
    public ResponseEntity<OrderDto> getOrderByIdOfClient(@RequestHeader(value = "Authorization") String token, @PathVariable long id_user, @PathVariable long id_order) throws Exception {
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

    @RequestMapping(value = "/updateOrder", method = RequestMethod.PUT)
    public ResponseEntity<OrderDto> updateOrder(@RequestHeader(value = "Authorization") String token, @RequestBody OrderDto orderDto) throws Exception {
        if (userAppService.getIdByToken(token) == orderDto.getProfile().getId()) {

        } throw new ApplicationException(ApplicationExceptionCause.NOT_ALLOW);
    }

    @RequestMapping(value = "/deleteOrder/{id_order}", method = RequestMethod.DELETE)
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

    @RequestMapping(value = "/updateProfile", method = RequestMethod.PUT)
    public ResponseEntity<ProfileDto> updateUser(@Valid @RequestBody ProfileDto profileDto) throws Exception {
        UserProfile profile = new ProfileBuilder(profileDto).getProfile();
        final UserProfile _profile = profileService.updateProfile(profile);
        return new ResponseEntity<>(
                new ProfileDtoBuilder(_profile).getProfileDto(),
                HttpStatus.OK
        );
    }

    @GetMapping("/profiles")
    public ResponseEntity<List<ProfileDto>> getAllProfiles() throws Exception {
        List<UserProfile> profiles = profileService.getAllUsers();
        return new ResponseEntity<>(
                profiles.stream().map(
                        profile -> new ProfileDtoBuilder(profile).getProfileDto())
                        .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<ProfileDto> getProfileById(@PathVariable long id) throws Exception {
        util.checkId(id);

        UserProfile profile = profileService.getUserProfileById(id);
        return new ResponseEntity<>(
                new ProfileDtoBuilder(profile).getProfileDto(),
                HttpStatus.OK
        );
    }
}
