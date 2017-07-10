(function () {

	angular.module('createLocal', ['ui.router','services'])
		.config(function ($stateProvider) {
			$stateProvider
				.state('createLocal', {
					url: '/createLocal',
					template: '<create-local></create-local>'
				})
		})
		.component('createLocal', {
			templateUrl: './states/local/createLocal.html',
			controller: function ($mdToast, $state, localsService) {
                var vm = this;
                vm.newLocal = new localsService.localsP();             
				vm.createLocal = function () {     
					vm.newLocal.$save()
						.then(function (result) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('¡Local creado correctamente!')
									.position('top right')
									.hideDelay(3000)
								);
							$state.go('getAllLocals');
						}, function (error) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('Error: el local no ha podido crearse.')
									.position('top right')
									.hideDelay(4000)
								);
						});
				};
			}
		})
}());