package com.how2java.tmall.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.xwork.math.RandomUtils;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.web.util.HtmlUtils;

import com.how2java.tmall.pojo.OrderItem;
import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.User;
import com.how2java.tmall.service.OrderService;
import com.how2java.tmall.service.ProductImageService;
import com.opensymphony.xwork2.ActionContext;
import com.sun.org.apache.bcel.internal.generic.NEW;

import tmall.comparator.ProductAllComparator;
import tmall.comparator.ProductDateComparator;
import tmall.comparator.ProductPriceComparator;
import tmall.comparator.ProductReviewComparator;
import tmall.comparator.ProductSaleCountComparator;

public class ForeAction extends Action4Result {
//	String msg;

	@Action("forehome")
	public String home() {
		categorys = categoryService.list();
		productService.fill(categorys);
		productService.fillByRow(categorys);
		return "home.jsp";
	}

	@Action("foreregister")
	public String register() {
		user.setName(HtmlUtils.htmlEscape(user.getName()));
		boolean exist = userService.isExist(user.getName());
		if (exist) {
			msg = "用户名已经存在，不能使用";
			return "register.jsp";
		}
		userService.save(user);
		return "registerSuccessPage";
	}

	@Action("forelogin")
	public String login() {
		user.setName(HtmlUtils.htmlEscape(user.getName()));
		User user_session = userService.get(user.getName(), user.getPassword());
		if (null == user_session) {
			msg = "账户密码错误";
			return "login.jsp";
		}
		ActionContext.getContext().getSession().put("user", user_session);
		return "homePage";
	}

	@Action("forelogout")
	public String logout() {
		ActionContext.getContext().getSession().remove("user");
		return "homePage";
	}

	@Action("foreproduct")
	public String product() {
		t2p(product);
		productImageService.setFirstProdutImage(product);
		productSingleImages = productImageService.list("product", product, "type", ProductImageService.type_single);
		productDetailImages = productImageService.list("product", product, "type", ProductImageService.type_detail);
		product.setProductSingleImages(productSingleImages);
		product.setProductDetailImages(productDetailImages);

		propertyValues = propertyValueService.listByParent(product);
		reviews = reviewService.listByParent(product);
		productService.setSaleAndReviewNumber(product);
		return "product.jsp";
	}

	@Action("forecheckLogin")
	public String checkLogin() {
		User u = (User) ActionContext.getContext().getSession().get("user");
		if (null == u)
			return "fail.jsp";
		else
			return "success.jsp";

	}

	@Action("foreloginAjax")
	public String loginAjax() {
		user.setName(HtmlUtils.htmlEscape(user.getName()));
		User user_session = userService.get(user.getName(), user.getPassword());
		if (null == user_session) {
			return "fail.jsp";
		}
		ActionContext.getContext().getSession().put("user", user_session);
		return "success.jsp";
	}

	@Action("forecategory")
	public String category() {
		t2p(category);
		productService.fill(category);
		productService.setSaleAndReviewNumber(category.getProducts());
		if (null != sort) {
			switch (sort) {
			case "review":
				Collections.sort(category.getProducts(), new ProductReviewComparator());
				break;
			case "date":
				Collections.sort(category.getProducts(), new ProductDateComparator());
				break;

			case "saleCount":
				Collections.sort(category.getProducts(), new ProductSaleCountComparator());
				break;

			case "price":
				Collections.sort(category.getProducts(), new ProductPriceComparator());
				break;

			case "all":
				Collections.sort(category.getProducts(), new ProductAllComparator());
				break;
			}
		}
		return "category.jsp";

	}

	@Action("foresearch")
	public String search() {
		products = productService.search(keyword, 0, 20);
		productService.setSaleAndReviewNumber(products);
		for (Product product : products) {
			productImageService.setFirstProdutImage(product);
		}
		return "searchResult.jsp";

	}

	@Action("forebuyone")
	public String buyone() {
		User user = (User) ActionContext.getContext().getSession().get("user");
		boolean found = false;
		List<OrderItem> ois = orderItemService.list("user", user, "order", null);
		for (OrderItem oi : ois) {
			if (oi.getProduct().getId() == product.getId()) {
				oi.setNumber(oi.getNumber() + num);
				orderItemService.update(oi);
				found = true;
				oiid = oi.getId();
				break;
			}

		}
		if (!found) {
			OrderItem oi = new OrderItem();
			oi.setUser(user);
			oi.setNumber(num);
			oi.setProduct(product);
			orderItemService.save(oi);
			oiid = oi.getId();
		}
		return "buyPage";
	}

	@Action("forebuy")
	public String buy() {
		orderItems = new ArrayList<OrderItem>();
		for (int oiid : oiids) {
			OrderItem oi = (OrderItem) orderItemService.get(oiid);
			total += oi.getProduct().getPromotePrice() * oi.getNumber();
			orderItems.add(oi);
			productImageService.setFirstProdutImage(oi.getProduct());
		}
		ActionContext.getContext().getSession().put("orderItems", orderItems);
		return "buy.jsp";
	}

	@Action("foreaddCart")
	public String addCart() {
		User user = (User) ActionContext.getContext().getSession().get("user");
		boolean found = false;
		List<OrderItem> ois = orderItemService.list("user", user, "order", null);
		for (OrderItem oi : ois) {
			if (oi.getProduct().getId() == product.getId()) {
				oi.setNumber(oi.getNumber() + num);
				orderItemService.update(oi);
				found = true;
//				oiid = oi.getId();
				break;
			}
		}
		if (!found) {
			OrderItem oi = new OrderItem();
			oi.setUser(user);
			oi.setNumber(num);
			oi.setProduct(product);
			orderItemService.save(oi);
//			oiid = oi.getId();
		}
		return "success.jsp";

	}

	@Action("forecart")
	public String cart() {
		User user = (User) ActionContext.getContext().getSession().get("user");
		orderItems = orderItemService.list("user", user, "order", null);
		for (OrderItem orderItem : orderItems) {
			productImageService.setFirstProdutImage(orderItem.getProduct());
		}
		return "cart.jsp";
	}

	@Action("forechangeOrderItem")
	public String changeOrderItem() {
		User user = (User) ActionContext.getContext().getSession().get("user");
		List<OrderItem> orderItems = orderItemService.list("user", user, "order", null);
		for (OrderItem orderItem : orderItems) {
			if (orderItem.getProduct().getId() == product.getId()) {
				orderItem.setNumber(num);
				orderItemService.update(orderItem);
				System.out.println(orderItem.getNumber());
				break;
			}
		}
		return "success.jsp";
	}

	@Action("foredeleteOrderItem")
	public String deleteOrderItem() {
		orderItemService.delete(orderItem);
		return "success.jsp";
	}

	@Action("forecreateOrder")
	public String createOrder() {
		List<OrderItem> ois = (List<OrderItem>) ActionContext.getContext().getSession().get("orderItems");
		if (ois.isEmpty())
			return "login.jsp";
		User user = (User) ActionContext.getContext().getSession().get("user");
		String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + RandomUtils.nextInt(10000);
		order.setOrderCode(orderCode);
		order.setCreateDate(new Date());
		order.setUser(user);
		order.setStatus(orderService.waitPay);
		total = orderService.createOrder(order, ois);
		return "alipayPage";

	}

	@Action("forealipay")
	public String forealipay() {
		return "alipay.jsp";
	}

	@Action("forepayed")
	public String payed() {
		t2p(order);
		order.setStatus(orderService.waitDelivery);
		order.setPayDate(new Date());
		orderService.update(order);
		return "payed.jsp";

	}

	@Action("forebought")
	public String bought() {
		User user = (User) ActionContext.getContext().getSession().get("user");
		orders = orderService.listByUserWithoutDelete(user);
		orderItemService.fill(orders);
		return "bought.jsp";
	}

	@Action("foreconfirmPay")
	public String confirmPay() {
		t2p(order);
		orderItemService.fill(order);
		return "confirmPay.jsp";
	}

	@Action("foreorderConfirmed")
	public String orderConfirmed() {
		t2p(order);
		order.setStatus(orderService.waitReview);
		order.setConfirmDate(new Date());
		orderService.update(order);
		return "orderConfirmed.jsp";
	}

	@Action("foredeleteOrder")
	public String deleteOrder() {
		t2p(order);
		order.setStatus(OrderService.delete);
		orderService.update(order);
		return "success.jsp";
	}

	@Action("forereview")
	public String review() {
		t2p(order);
		orderItemService.fill(order);
		product = order.getOrderItems().get(0).getProduct();
		reviews = reviewService.listByParent(product);
		productService.setSaleAndReviewNumber(product);
		return "review.jsp";
	}

	@Action("foredoreview")
	public String doreview() {
		t2p(order);
		t2p(product);
		order.setStatus(orderService.finish);
		String content = review.getContent();
		content = HtmlUtils.htmlEscape(content);
		User user = (User) ActionContext.getContext().getSession().get("user");
		review.setContent(content);
		review.setProduct(product);
		review.setCreateDate(new Date());
		review.setUser(user);
		reviewService.saveReviewAndUpdateOrderStatus(review, order);
		showonly = true;
		return "reviewPage";
	}
}
