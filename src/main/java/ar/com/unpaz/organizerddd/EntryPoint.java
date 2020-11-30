package ar.com.unpaz.organizerddd;

import java.awt.EventQueue;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import ar.com.unpaz.organizerddd.application.services.AppServicePass;
import ar.com.unpaz.organizerddd.application.services.AppServiceUser;
import ar.com.unpaz.organizerddd.application.services.AppServices;
import ar.com.unpaz.organizerddd.domain.entitys.Password;
import ar.com.unpaz.organizerddd.domain.entitys.User;
import ar.com.unpaz.organizerddd.domain.repositorycontracts.IRepository;
import ar.com.unpaz.organizerddd.domain.services.DomainPasswordServices;
import ar.com.unpaz.organizerddd.domain.services.DomainUserService;
import ar.com.unpaz.organizerddd.domain.services.IDomainServices;
import ar.com.unpaz.organizerddd.infrastructure.db.postgresql.PostgreslqPasswordRepository;
import ar.com.unpaz.organizerddd.infrastructure.db.postgresql.PostgreslqUserRepository;
import ar.com.unpaz.organizerddd.infrastructure.inmem.InMemoryPasswordRepository;
import ar.com.unpaz.organizerddd.infrastructure.inmem.InMemoryUserRepository;
import ar.com.unpaz.organizerddd.presentation.controllers.IController;
import ar.com.unpaz.organizerddd.presentation.controllers.Selector;
import ar.com.unpaz.organizerddd.presentation.controllers.SelectorImp;
import ar.com.unpaz.organizerddd.presentation.controllers.ViewAdminController;
import ar.com.unpaz.organizerddd.presentation.controllers.ViewPasswordController;
import ar.com.unpaz.organizerddd.presentation.loginview.LoginView;
import ar.com.unpaz.organizerddd.presentation.loginview.LoginViewOperations;
import ar.com.unpaz.organizerddd.presentation.mainview.AdminView;
import ar.com.unpaz.organizerddd.presentation.mainview.MainView;
import ar.com.unpaz.organizerddd.presentation.mainview.MainViewOperations;
import ar.com.unpaz.organizerddd.presentation.validator.IValidator;
import ar.com.unpaz.organizerddd.presentation.validator.PasswordFrontValidator;
import ar.com.unpaz.organizerddd.presentation.validator.UserFrontValidator;
import ar.com.unpaz.organizerddd.transversalinfrastructure.EnviromentVariables;
import ar.com.unpaz.organizerddd.transversalinfrastructure.login.LoginController;
import ar.com.unpaz.organizerddd.transversalinfrastructure.login.LoginControllerImp;

public class EntryPoint {
	public static void main(String[] args) {
		/*
		 * IMPLEMENTACION SERVICE LOCATOR DINAMICO
		 * 
		 * Iservice service=new ServiceLocator(); Iservice serviceTest=new
		 * ServiceLocator();
		 */
		///

		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		EventQueue.invokeLater(() -> {

			IRepository<Password> passwordrepository = new PostgreslqPasswordRepository();
			IRepository<User> userrepository = new PostgreslqUserRepository();

			if (userrepository instanceof InMemoryUserRepository
					&& passwordrepository instanceof InMemoryPasswordRepository) {
				EnviromentVariables.INMEMORY = true;
			}

			IDomainServices<Password> passdomainservices = new DomainPasswordServices();
			IDomainServices<User> userdomainservices = new DomainUserService();

			AppServices<Password> appservicepass = new AppServicePass(passwordrepository, passdomainservices);
			AppServices<User> appserviceuser = new AppServiceUser(userrepository, userdomainservices);

			IValidator<Password> passfrontValidator = new PasswordFrontValidator();
			IValidator<User> userfrontValidator = new UserFrontValidator();

			MainViewOperations<Password> mainview = new MainView();
			MainViewOperations<User> adminview = new AdminView();

			IController<Password> viewpasscontroller = new ViewPasswordController(appservicepass, mainview,
					passfrontValidator);
			IController<User> viewadmincontroller = new ViewAdminController(appserviceuser, adminview,
					userfrontValidator);

			Selector selector = new SelectorImp(viewpasscontroller, viewadmincontroller, appserviceuser,
					appservicepass);

			LoginViewOperations loginview = new LoginView();

			LoginController logincontroller = new LoginControllerImp(loginview, selector);

			logincontroller.startView();

		});

	}
}
