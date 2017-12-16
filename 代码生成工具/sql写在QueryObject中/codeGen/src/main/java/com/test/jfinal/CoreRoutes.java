package com.test.jfinal;

import com.jfinal.config.Routes;
import com.test.web.controller.AuthrecordController;
import com.test.web.controller.CustomerController;
import com.test.web.controller.HotelController;
import com.test.web.controller.LogininfoController;
import com.test.web.controller.ManagerController;
import com.test.web.controller.SystemdicController;
import com.test.web.controller.SystemdicitemController;
public class CoreRoutes extends Routes{

	@Override
	public void config() {
		add("/authrecord", AuthrecordController.class);
		add("/customer", CustomerController.class);
		add("/hotel", HotelController.class);
		add("/logininfo", LogininfoController.class);
		add("/manager", ManagerController.class);
		add("/systemdic", SystemdicController.class);
		add("/systemdicitem", SystemdicitemController.class);
	}

}
