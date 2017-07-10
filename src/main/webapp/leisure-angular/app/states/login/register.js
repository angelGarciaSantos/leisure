(function () {

	angular.module('register', ['ui.router','services'])
		.config(function ($stateProvider) {
			$stateProvider
				.state('register', {
					url: '/register',
					template: '<register></register>'
				})
		})
		.component('register', {
			templateUrl: './states/login/register.html',
			controller: function ($mdToast, $state, usersService) {
                var vm = this;
                vm.user = new usersService.register();             
                vm.registerUser = function () {
                    if (vm.user.password != vm.passwordCheck) {
                        $mdToast.show(
                            $mdToast.simple()
                                .textContent('Error: las contraseñas no coinciden.')
                                .position('top right')
                                .hideDelay(4000)
                            );
                    }
                    else {
                        vm.user.$save()
                            .then(function (result) {
                                $mdToast.show(
                                    $mdToast.simple()
                                        .textContent('¡Registro correcto!')
                                        .position('top right')
                                        .hideDelay(3000)
                                    );
                                $state.go('home');
                            }, function (error) {
                                $mdToast.show(
                                    $mdToast.simple()
                                        .textContent('Error: no pudo registrarse.')
                                        .position('top right')
                                        .hideDelay(4000)
                                    );
                            });

                    }
				};
			}
		})

}());