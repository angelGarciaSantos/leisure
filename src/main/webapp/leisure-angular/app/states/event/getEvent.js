(function () {

	angular.module('getEvent', ['services'])
		.config(function ($stateProvider) {
			$stateProvider
				.state('getEvent', {
					url: '/getEvent/:id',
					template: '<get-event></get-event>'
				})
		})
		.component('getEvent', {
			templateUrl: './states/event/getEvent.html',
			controller: function ($mdToast, NgMap, eventsService, artistsService, localsService, commentsService, ratingsService, interestsService, usersService, $state, $stateParams, $mdDialog) {
                var vm = this;
				vm.firstEventArtists = 0;
				vm.maxEventArtists = 3;
				vm.showNextButtonEventArtist = false;
				vm.showPreviousButtonEventArtist = false;

				vm.firstEventComments = 0;
				vm.maxEventComments = 5;
				vm.showNextButtonEventComment = false;
				vm.showPreviousButtonEventComment = false;

				vm.firstEventRatings = 0;
				vm.maxEventRatings = 5;
				vm.showNextButtonEventRating = false;
				vm.showPreviousButtonEventRating = false;

                vm.hideDialog = $mdDialog.hide;
				vm.loginInfo = [];
				vm.local;

				vm.prueba = new Date();

				usersService.loginInfo.query().$promise.then(function(data) {
					vm.loginInfo = data;
					if(vm.loginInfo.length > 0) {
						vm.userId = vm.loginInfo[0];
						vm.isFollowing();
					}
					else {
						$mdToast.show(
							$mdToast.simple()
								.textContent('¡Inicia sesión para seguir eventos!')
								.position('top right')
								.hideDelay(4000)
							);

					}
				});
				
                vm.eventId = $stateParams.id;

				eventsService.events.get({ id: vm.eventId }).$promise.then(function(data) {
					vm.event = data;
					vm.event.beginDate = new Date(vm.event.beginDate);
					vm.event.endDate = new Date(vm.event.endDate);
					localsService.locals.get({ id: vm.event.local.id }).$promise.then(function(data2) {
						vm.local = data2;
						vm.locals = [];
						vm.locals.push(vm.local);
					}, function (error) {
							setTimeout(function() {throw error});
            //using setTimeout moves the actual throwingout of the
            // promise chain. It's kind of a hack!
            				return $promise.reject(error) 
						});
				});				
				
				vm.newRating = new ratingsService.rateEvent();  
				vm.newComment = new commentsService.commentEvent();  
				
				vm.isFollowing = function(){
					eventsService.followEvent.query({ eventId: vm.eventId, userId: vm.loginInfo[0] }).$promise.then(function(data) {
						var result = data;
						vm.isFollowingEvent = result[0]; 
						eventsService.followEvent.query({ eventId: vm.eventId, userId: vm.loginInfo[0] }).$promise.then(function(data2) {
							var result2 = data2;
							vm.isFollowingEvent = result2[0];
							eventsService.followEvent.query({ eventId: vm.eventId, userId: vm.loginInfo[0] }).$promise.then(function(data3) {
								var result3 = data3;
								vm.isFollowingEvent = result3[0]; 		
							});	 		
						});	
					});	
				}

				vm.deleteEvent = function () {
					vm.event.$delete({ id: vm.eventId })
						.then(function (result) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('¡Evento eliminado correctamente!')
									.position('top right')
									.hideDelay(3000)
								);
							$state.go('getAllEvents');
						}, function (error) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('Error: no ha podido eliminarse el evento.')
									.position('top right')
									.hideDelay(4000)
								);
						});
				};

				vm.follow = new eventsService.followEvent(); 

				vm.followEvent = function () {
					vm.follow.$save({ eventId: vm.eventId, userId: vm.userId })
						.then(function (result) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('¡Evento seguido correctamente!')
									.position('top right')
									.hideDelay(3000)
								);
							vm.isFollowing();	
						}, function (error) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('Error: no ha podido seguirse el evento.')
									.position('top right')
									.hideDelay(4000)
								);
						});
				}

				vm.unfollowEvent = function () {
					vm.follow.$delete({ eventId: vm.eventId, userId: vm.userId })
						.then(function (result) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('¡Se ha dejado de seguir el evento correctamente!')
									.position('top right')
									.hideDelay(3000)
								);
							vm.isFollowing();
						}, function (error) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('Error: no ha podido dejarse de seguir el evento.')
									.position('top right')
									.hideDelay(4000)
								);
						});
				}

				//vm.globalRating = ratingsService.globalRating.get({ id: vm.eventId });

				ratingsService.globalRatingEvent.query({ id: vm.eventId }).$promise.then(function(data) {
					vm.globalRating = data;
				});


				// vm.commentEvent = function () {
				// 	var params = { event: vm.eventId, user: 1 };
				// 	$state.go('commentEvent', params);
				// };
           
                vm.commentEvent = function () {
					//vm.nuevoMovimiento.fecha = new Date(vm.nuevoMovimiento.fecha);
                    vm.newComment.$save({ eventId: vm.eventId, userId: vm.userId})
						.then(function (result) {
							vm.newComment.text = "";
							$mdToast.show(
								$mdToast.simple()
									.textContent('¡Comentario creado correctamente!')
									.position('top right')
									.hideDelay(3000)
								);
							vm.reloadEventComments();
						}, function (error) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('Error: no ha podido crearse el comentario.')
									.position('top right')
									.hideDelay(4000)
								);
							//vm.nuevoMovimiento.importe = -9999;
						});
				};


				
				//vm.showDialog = vm.showDialog;
				vm.commentEventDialog = function(evt) {
					vm.dialogOpen = true;
					$mdDialog.show({
						targetEvent: evt,
						controller: function () { 
							this.parent = vm; 
						},
						controllerAs: 'ctrl',
						templateUrl: './states/event/commentEventDialog.html'
					});
				};

				// vm.rateEvent = function () {
				// 	//CAMBIAR AL ESTADO COMMENTEVENT PASANDOLE EL eventId y userId
				// 	var params = { event: vm.eventId, user: 1 };
				// 	$state.go('rateEvent', params);
				// };

				           
                vm.rateEvent = function () {
					//vm.nuevoMovimiento.fecha = new Date(vm.nuevoMovimiento.fecha);              
                    vm.newRating.$save({ eventId: vm.eventId, userId: vm.userId })
						.then(function (result) {
							
							
							$mdToast.show(
								$mdToast.simple()
									.textContent('¡Evento valorado correctamente!')
									.position('top right')
									.hideDelay(3000)
								);
							vm.reloadEventRatings();
						
							
						}, function (error) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('Error: no ha podido valorarse el evento.')
									.position('top right')
									.hideDelay(3000)
								);
							//vm.nuevoMovimiento.importe = -9999;
						});
				};
				vm.newInterest = new interestsService.interestsByEvent();
				vm.interestEvent = function () {
					//llamar al servicio para interests para el evento
					vm.newInterest.$save({ eventId: vm.eventId, userId: vm.userId })
						.then(function (result) {
							// cuando ha terminado el guardado del movimiento
							// es momento de pedir una actualización de datos
							//vm.nuevoMovimiento.importe = 0;
						}, function (error) {
							console.error(error);
							//vm.nuevoMovimiento.importe = -9999;
						});
				};

				vm.rateEventDialog = function(evt) {
					vm.dialogOpen = true;
					$mdDialog.show({
						targetEvent: evt,
						controller: function () { 
							this.parent = vm; 
						},
						controllerAs: 'ctrl',
						templateUrl: './states/event/rateEventDialog.html'
					});
				};

				vm.reloadEventArtists = function () {
					artistsService.artistsByEvent.query({ id: vm.eventId, first: vm.firstEventArtists, max: vm.maxEventArtists+1 }).$promise.then(function(data) {
						vm.eventArtists = data;	
						artistsService.artistsByEvent.query({ id: vm.eventId, first: vm.firstEventArtists, max: vm.maxEventArtists+1 }).$promise.then(function(data) {
							vm.eventArtists = data;	
							artistsService.artistsByEvent.query({ id: vm.eventId, first: vm.firstEventArtists, max: vm.maxEventArtists+1 }).$promise.then(function(data) {
								vm.eventArtists = data;

								if(vm.firstEventArtists != 0) {
									vm.showPreviousButtonEventArtist = true;
								}
								else {
									vm.showPreviousButtonEventArtist = false;
								}

								if(vm.eventArtists.length > 3) {
									vm.showNextButtonEventArtist = true;
									vm.eventArtists.splice(vm.eventArtists.length -1, 1);
								}
								else{
									vm.showNextButtonEventArtist = false;
								}
							});
						});
					});
				};

				vm.getNextEventArtist = function() {								
					vm.firstEventArtists += 3;
					vm.reloadEventArtists();					
				}

				vm.getPreviousEventArtist = function() {
					vm.firstEventArtists -= 3;
					if(vm.firstEventArtists < 0){
						vm.firstEventArtists = 0;
					}
					vm.reloadEventArtists();
				}

				vm.reloadEventComments = function () {
					commentsService.commentsByEvent.query({ id: vm.eventId, first: vm.firstEventComments, max: vm.maxEventComments+1 }).$promise.then(function(data) {
						vm.eventComments = data;

						if(vm.firstEventComments != 0) {
							vm.showPreviousButtonEventComment = true;
						}
						else {
							vm.showPreviousButtonEventComment = false;
						}

						if(vm.eventComments.length > 5) {
							vm.showNextButtonEventComment = true;
							vm.eventComments.splice(vm.eventComments.length -1, 1);
						}
						else{
							vm.showNextButtonEventComment = false;
						}
							
						for (i=0;i<vm.eventComments.length;i++){
							vm.eventComments[i].date = new Date(vm.eventComments[i].date);
						}
						
					});
				};

				vm.getNextEventComment = function() {								
					vm.firstEventComments += 5;
					vm.reloadEventComments();					
				}

				vm.getPreviousEventComment = function() {
					vm.firstEventComments -= 5;
					if(vm.firstEventComments < 0){
						vm.firstEventComments = 0;
					}
					vm.reloadEventComments();
				}

				vm.reloadEventRatings = function () {
					ratingsService.ratingsByEvent.query({ id: vm.eventId, first: vm.firstEventRatings, max: vm.maxEventRatings+1 }).$promise.then(function(data) {
						vm.eventRatings = data;

						if(vm.firstEventRatings != 0) {
							vm.showPreviousButtonEventRating = true;
						}
						else {
							vm.showPreviousButtonEventRating = false;
						}

						if(vm.eventRatings.length > 5) {
							vm.showNextButtonEventRating = true;
							vm.eventRatings.splice(vm.eventRatings.length -1, 1);
						}
						else{
							vm.showNextButtonEventRating = false;
						}
						ratingsService.globalRatingEvent.query({ id: vm.eventId }).$promise.then(function(data2) {
							vm.globalRating = data2;
						});
							
						for (i=0;i<vm.eventRatings.length;i++){
							vm.eventRatings[i].date = new Date(vm.eventRatings[i].date);
						}
					});
				};

				vm.getNextEventRating = function() {								
					vm.firstEventRatings += 5;
					vm.reloadEventRatings();					
				}

				vm.getPreviousEventRating = function() {
					vm.firstEventRatings -= 5;
					if(vm.firstEventRatings < 0){
						vm.firstEventRatings = 0;
					}
					vm.reloadEventRatings();
				}

				vm.artistDetails = function (id) {
					var params = { id: id };
					$state.go('getArtist', params);
				};

				vm.localDetails = function (id) {
					var params = { id: id };
					$state.go('getLocal', params);
				}

				vm.reloadEventArtists();
				vm.reloadEventComments();
				vm.reloadEventRatings();
			}
		})

}());