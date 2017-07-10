(function () {

	angular.module('updateArtist', ['ui.router', 'services'])
		.config(function ($stateProvider) {
			$stateProvider
				.state('updateArtist', {
					url: '/updateArtist/:id',
					template: '<update-artist></update-artist>'
				})
		})
		.component('updateArtist', {
			templateUrl: './states/artist/updateArtist.html',
			controller: function ($mdToast, artistsService, $state, $stateParams) {
                var vm = this;

                var artistId = $stateParams.id;
                vm.editArtist = artistsService.artists.get({ id: artistId });
				vm.sendArtist = new artistsService.artistsP();  

                vm.updateArtist = function () {
					//vm.nuevoMovimiento.fecha = new Date(vm.nuevoMovimiento.fecha);  
					vm.sendArtist.id = vm.editArtist.id;
					vm.sendArtist.name = vm.editArtist.name;
					vm.sendArtist.description = vm.editArtist.description;
					vm.sendArtist.image = vm.editArtist.image;


                    vm.sendArtist.$update({ id: artistId })
						.then(function (result) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('¡Artista actualizado correctamente!')
									.position('top right')
									.hideDelay(3000)
								);
							$state.go('getAllArtists');
						}, function (error) {
							console.error(error);
								$mdToast.show(
									$mdToast.simple()
										.textContent('Error: el artista no ha podido actualizarse.')
										.position('top right')
										.hideDelay(4000)
									);
						});
				};
			}
		})

}());