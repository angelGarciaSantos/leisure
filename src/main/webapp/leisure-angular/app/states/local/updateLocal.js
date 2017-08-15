(function () {

	angular.module('updateLocal', ['ui.router', 'services'])
		.config(function ($stateProvider) {
			$stateProvider
				.state('updateLocal', {
					url: '/updateLocal/:id',
					template: '<update-local></update-local>'
				})
		})
		.component('updateLocal', {
			templateUrl: './states/local/updateLocal.html',
			controller: function (localsService, $state, $stateParams) {
                var vm = this;

                vm.localId = $stateParams.id;
				vm.sendLocal = new localsService.localsP();  
                vm.editLocal = localsService.locals.get({ id: vm.localId });

                vm.updateLocal = function () {
					vm.sendLocal.id = vm.editLocal.id;
					vm.sendLocal.name = vm.editLocal.name;
					vm.sendLocal.description = vm.editLocal.description;
					vm.sendLocal.capacity = vm.editLocal.capacity;
					vm.sendLocal.lat = vm.editLocal.lat;
					vm.sendLocal.lng = vm.editLocal.lng;
					vm.sendLocal.image = vm.editLocal.image;
					//vm.nuevoMovimiento.fecha = new Date(vm.nuevoMovimiento.fecha);              
                    vm.sendLocal.$update({ id: vm.localId })
						.then(function (result) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('¡Local actualizado correctamente!')
									.position('top right')
									.hideDelay(3000)
								);
							var params = { id: vm.localId };
							$state.go('getLocal', params);
						}, function (error) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('Error: no ha podido actualizarse el local.')
									.position('top right')
									.hideDelay(4000)
								);
							//vm.nuevoMovimiento.importe = -9999;
						});
				};
			}
		})

}());