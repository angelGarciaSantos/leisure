(function () {

	angular.module('login', ['ui.router','services'])
		.config(function ($stateProvider) {
			$stateProvider
				.state('login', {
					url: '/login',
					template: '<login></login>'
				})
		})
		.component('login', {
			templateUrl: './states/login/login.html',
			controller: function ($mdToast, $state, usersService) {
                var vm = this;
                vm.user = new usersService.login();             
                vm.loginUser = function () {
					//vm.nuevoMovimiento.fecha = new Date(vm.nuevoMovimiento.fecha);              
					vm.user.$save()
						.then(function (result) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('¡Login correcto!')
									.position('top right')
									.hideDelay(3000)
								);
							$state.go('home');
						}, function (error) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('Error: no se pudo hacer login.')
									.position('top right')
									.hideDelay(4000)
								);
						});
				};
			}
		})

}());