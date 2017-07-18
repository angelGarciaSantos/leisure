(function () {

	angular.module('home', ['ui.router','ngMaterial','ngAnimate','ngAria'])
		.config(function ($stateProvider) {
			$stateProvider
				.state('home', {
					url: '/',
					template: '<home></home>'
				})
		})
		.component('home', {
			templateUrl: './states/home/home.html',
			controller: function ($state,$mdToast, interestsService, artistsService, eventsService, usersService) {
                var vm = this;
				vm.user = new usersService.logout(); 
				vm.loginInfo = [];
				vm.firstArtist = 0;
				vm.maxArtist = 5;
				vm.showNextButtonArtist = false;
				vm.showPreviousButtonArtist = false;
				vm.firstEvent = 0;
				vm.maxEvent = 5;
				vm.showNextButtonEvent = false;
				vm.showPreviousButtonEvent = false;

				usersService.loginInfo.query().$promise.then(function(data) {
					vm.loginInfo = data;
					if (vm.loginInfo.length > 0) {

						vm.loadArtists = function () {
							artistsService.artistsByUser.query({id: vm.loginInfo[0], first: vm.firstArtist, max: (vm.maxArtist+1)}).$promise.then(function(data) {
								vm.artistsByUser = data;

								if(vm.firstArtist != 0) {
									vm.showPreviousButtonArtist = true;
								}
								else {
									vm.showPreviousButtonArtist = false;
								}

								if(vm.artistsByUser.length > 5) {
									vm.showNextButtonArtist = true;
									vm.artistsByUser.splice(vm.artistsByUser.length -1, 1);
								}
								else{
									vm.showNextButtonArtist = false;
								}
							});
						}	

						vm.getNextArtist = function() {				
							vm.firstArtist += 5;
							vm.loadArtists();				
						}

						vm.getPreviousArtist = function() {						
							vm.firstArtist -= 5;
							if(vm.firstArtist < 0){
								vm.firstArtist = 0;
							}
							vm.loadArtists();
						}

						vm.loadEvents = function () {
							eventsService.eventsByUser.query({id: vm.loginInfo[0], first: vm.firstEvent, max: (vm.maxEvent+1)}).$promise.then(function(data) {
								vm.eventsByUser = data;

								if(vm.firstEvent != 0) {
									vm.showPreviousButtonEvent = true;
								}
								else {
									vm.showPreviousButtonEvent = false;
								}

								if(vm.eventsByUser.length > 5) {
									vm.showNextButtonEvent = true;
									vm.eventsByUser.splice(vm.eventsByUser.length -1, 1);
								}
								else{
									vm.showNextButtonEvent = false;
								}
							});
						}	

						vm.getNextEvent = function() {				
							vm.firstEvent += 5;
							vm.loadEvents();				
						}

						vm.getPreviousEvent = function() {						
							vm.firstEvent -= 5;
							if(vm.firstEvent < 0){
								vm.firstEvent = 0;
							}
							vm.loadEvents();
						}

						artistsService.recommendedArtists.query({id: vm.loginInfo[0]}).$promise.then(function(data) {
							vm.recommendedArtists = data;
						});

						eventsService.recommendedEvents.query({id: vm.loginInfo[0]}).$promise.then(function(data) {
							vm.recommendedEvents = data;
						});

						vm.loadArtists();
						vm.loadEvents();
					}
					else {
						$mdToast.show(
							$mdToast.simple()
								.textContent('¡Inicia sesión para ver recomendaciones de artistas y eventos!')
								.position('top right')
								.hideDelay(4000)
							);

					}
				});

				vm.logout = function () {
					//vm.nuevoMovimiento.fecha = new Date(vm.nuevoMovimiento.fecha);              
					vm.user.$delete()
						.then(function (result) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('¡Logout correcto!')
									.position('top right')
									.hideDelay(3000)
								);
								usersService.loginInfo.query().$promise.then(function(data) {
									vm.loginInfo = data;
									vm.recommendedArtists = [];	
								});
						}, function (error) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('Error: no se pudo hacer logout.')
									.position('top right')
									.hideDelay(4000)
								);
						});
				};            
				
				//TODO: de momento userId va forzado a 1.

				vm.artistDetails = function (id) {
					var params = { id: id };
					$state.go('getArtist', params);
				};

				vm.eventDetails = function (id) {
					var params = { id: id };
					$state.go('getEvent', params);
				};


				// function sortFunction(a, b) {
				// 	if (a[1] === b[1]) {
				// 		return 0;
				// 	}
				// 	else {
				// 		return (a[1] > b[1]) ? -1 : 1;
				// 	}
				// }



				// //TODO: en lugar de forzar el 1 recuperar el id del usuario actual.
				// interestsService.interestsByUser.query({id: 1}).$promise.then(function(data) {
				// 	vm.interests = data;

				// 	vm.tagsPoints = [];
				// 	for (i=0;i<vm.interests.length;i++) {
				// 		vm.tagsPoints.push([vm.interests[i].tag.id,vm.interests[i].points]);
				// 	}
				// 	vm.tagsPoints.sort(sortFunction);
					
				// 	artistsService.artists.query().$promise.then(function(data2) {
				// 		vm.allArtists = data2;
				// 		//falta rellenar artistas points y luego sumarle
				// 		vm.artistsPoints = [];
				// 		for (i=0;i<vm.allArtists.length;i++){
				// 			//por cada tag del artista buscar en tagsPoints el id y 
				// 			//sumar esos points a una variable, luego pasarla
				// 			//a artistsPoints
				// 			var sum = 0;
				// 			for(j=0;j<vm.allArtists[i].tags.length;j++){
				// 				for(k=0;k<vm.tagsPoints.length;k++){
				// 					if(vm.tagsPoints[k][0] == vm.allArtists[i].tags[j].id){
				// 						sum += vm.tagsPoints[k][1];
				// 					}
				// 				}
				// 			}
				// 			vm.artistsPoints.push([vm.allArtists[i].id, sum]);
				// 		}
				// 		var prueba = vm.artistsPoints;
				// 		vm.artistsPoints.sort(sortFunction);

				// 		//Ahora ya tenemos los artistas recomendados (los ids).
				// 		//Los recuperamos uno a uno y los mostramos en una lista.
				// 		vm.recomendedArtists = [];
				// 		for (i=0;i<vm.artistsPoints.length;i++) {
				// 			artistsService.artists.get({id: vm.artistsPoints[i][0]}).$promise.then(function(data) {
				// 				vm.recomendedArtists.push(data)
				// 			});
				// 		}
				// 	});



				// });









			}
		})

}());