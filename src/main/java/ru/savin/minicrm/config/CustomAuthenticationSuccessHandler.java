package ru.savin.minicrm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ru.savin.minicrm.entity.User;
import ru.savin.minicrm.service.UserService;

import javax.servlet.http.HttpServletRequest; // todo optimize imports
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException { // todo ошибку лучше не прокидывать тут, а обработать

		String userName = authentication.getName();

		User theUser = userService.findByUserName(userName); // todo списал откуда то? так себе идея добавлять the к имени объекта

		HttpSession session = request.getSession();
		session.setAttribute("user", theUser);
		session.setMaxInactiveInterval(3600);

		response.sendRedirect(request.getContextPath() + "/");
	}

}
