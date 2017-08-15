(function () {

	angular.module('getArtist', ['services'])
		.config(function ($stateProvider) {
			$stateProvider
				.state('getArtist', {
					url: '/getArtist/:id',
					template: '<get-artist></get-artist>'
				})
		})
		.component('getArtist', {
			templateUrl: './states/artist/getArtist.html',
			controller: function (artistsService, usersService, eventsService, $state, $mdToast, $stateParams, ratingsService, tagsService) {
                var vm = this;
                vm.artistId = $stateParams.id;
				vm.loginInfo = [];
				vm.isFollowingArtist = false;
				vm.delete = new artistsService.artistsP();  

				usersService.loginInfo.query().$promise.then(function(data) {
					vm.loginInfo = data;
					if (vm.loginInfo.length > 0) {
						vm.isFollowing();
					}
					else {
						$mdToast.show(
							$mdToast.simple()
								.textContent('¡Inicia sesión para seguir a artistas!')
								.position('top right')
								.hideDelay(4000)
							);

					}
				});


				vm.isFollowing = function(){
					artistsService.followArtist.query({ artistId: vm.artistId, userId: vm.loginInfo[0] }).$promise.then(function(data) {
						var result = data;
						vm.isFollowingArtist = result[0]; 
					});	
				}

				vm.loadArtist = function() {
					artistsService.artists.get({ id: vm.artistId  }).$promise.then(function(data) {
						vm.artist = data;
						tagsService.tagsByArtist.query({ id: vm.artistId }).$promise.then(function(data) {
							vm.tags = data;	
						});	
						eventsService.eventsByArtist.query({id: vm.artistId}).$promise.then(function(data) {
							vm.nextEvents = data;	
						});						
					});
				};

				vm.eventDetails = function (id) {
					var params = { id: id };
					$state.go('getEvent', params);
				}

				vm.tagDetails = function (id) {
					var params = { id: id };
					$state.go('getTag', params);
				}
				
				vm.deleteArtist = function () {
					vm.delete.$delete({ id: vm.artistId })
						.then(function (result) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('¡Artista eliminado correctamente!')
									.position('top right')
									.hideDelay(3000)
								);
							$state.go('getAllArtists');
						}, function (error) {
							if(error.status == 423) {
								$mdToast.show(
									$mdToast.simple()
										.textContent('No se puede borrar: El artista tiene eventos, tags o usuarios relacionados.')
										.position('top right')
										.hideDelay(4000)
								);
							}
							else{
								$mdToast.show(
								$mdToast.simple()
									.textContent('No se puede borrar: Error interno del servidor.')
									.position('top right')
									.hideDelay(4000)
								);
							}
						});
				}

				vm.loadRating = function () {
					ratingsService.globalRatingArtist.query({ id: vm.artistId }).$promise.then(function(data) {
						vm.globalRating = data;
					});
				};

				vm.loadArtist();
				vm.loadRating();



				
				vm.follow = new artistsService.followArtist(); 

				vm.followArtist = function () {
					vm.follow.$save({ artistId: vm.artistId, userId: vm.loginInfo[0] })
						.then(function (result) {
							vm.isFollowing();	
							$mdToast.show(
								$mdToast.simple()
									.textContent('¡Artista seguido correctamente!')
									.position('top right')
									.hideDelay(3000)
								);
						}, function (error) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('Error: no se pudo seguir al artista.')
									.position('top right')
									.hideDelay(4000)
								);
						});
				}

				vm.unfollowArtist = function () {
					vm.follow.$delete({ artistId: vm.artistId, userId: vm.loginInfo[0] })
						.then(function (result) {
							vm.isFollowing();
							$mdToast.show(
								$mdToast.simple()
									.textContent('¡Has dejado de seguir al artista correctamente!')
									.position('top right')
									.hideDelay(3000)
								);
						}, function (error) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('Error: no ha podido dejar de seguirse al artista.')
									.position('top right')
									.hideDelay(4000)
								);
						});
				}

				vm.$onInit = function() {

				};
			}
		})

}());