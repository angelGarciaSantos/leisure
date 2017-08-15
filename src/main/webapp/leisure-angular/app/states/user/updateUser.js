(function () {

	angular.module('updateUser', ['ui.router', 'services'])
		.config(function ($stateProvider) {
			$stateProvider
				.state('updateUser', {
					url: '/updateUser/:id',
					template: '<update-user></update-user>'
				})
		})
		.component('updateUser', {
			templateUrl: './states/user/updateUser.html',
			controller: function (usersService, $state, $stateParams) {
                var vm = this;

                var userId = $stateParams.id;
                vm.editUser = usersService.users.get({ id: userId });

                vm.updateUser = function () {
					//vm.nuevoMovimiento.fecha = new Date(vm.nuevoMovimiento.fecha);              
                    vm.editUser.$update({ id: userId })
						.then(function (result) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('¡Usuario actualizado correctamente!')
									.position('top right')
									.hideDelay(3000)
								);
							$state.go("getAllUsers");
						}, function (error) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('Error: el usuario no ha podido actualizarse.')
									.position('top right')
									.hideDelay(4000)
								);
							//vm.nuevoMovimiento.importe = -9999;
						});
				};
			}
		})

}());