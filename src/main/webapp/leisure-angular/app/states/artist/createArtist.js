(function () {

	angular.module('createArtist', ['ui.router','services'])
		.config(function ($stateProvider) {
			$stateProvider
				.state('createArtist', {
					url: '/createArtist',
					template: '<create-artist></create-artist>'
				})
		})
		.component('createArtist', {
			templateUrl: './states/artist/createArtist.html',
			controller: function ($mdToast, $state, artistsService) {
                var vm = this;
                vm.newArtist = new artistsService.artistsP();             
                vm.createArtist = function () {
					//vm.nuevoMovimiento.fecha = new Date(vm.nuevoMovimiento.fecha);              
                    vm.newArtist.$save()
						.then(function (result) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('¡Artista creado correctamente!')
									.position('top right')
									.hideDelay(3000)
								);
							$state.go('getAllArtists');
						}, function (error) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('Error: el artista no ha podido crearse.')
									.position('top right')
									.hideDelay(4000)
								);
						});
				};
			}
		})

}());