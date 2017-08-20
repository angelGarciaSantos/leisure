(function () {

	angular.module('updateEvent', ['ui.router', 'services'])
		.config(function ($stateProvider) {
			$stateProvider
				.state('updateEvent', {
					url: '/updateEvent/:id',
					template: '<update-event></update-event>'
				})
		})
		.component('updateEvent', {
			templateUrl: './states/event/updateEvent.html',
			controller: function ($timeout, $scope, $mdDialog, $mdpTimePicker, eventsService, artistsService, localsService, $state, $stateParams, $mdToast) {
                var vm = this;
				
				vm.firstEventArtists = 0;
				vm.maxEventArtists = 3;
				vm.showNextButtonEventArtist = false;
				vm.showPreviousButtonEventArtist = false;
				vm.keywordsLocal = "";
				vm.firstLocal = 0;
				vm.maxLocal = 3;
				vm.firstSearchLocal = 0;
				vm.maxSearchLocal = 3;
				vm.showNextButtonLocal = false;
				vm.showPreviousButtonLocal = false;
				vm.keywordsArtist = "";
				vm.firstArtist = 0;
				vm.maxArtist = 3;
				vm.firstSearchArtist = 0;
				vm.maxSearchArtist = 3;
				vm.showNextButtonArtist = false;
				vm.showPreviousButtonArtist = false;

				vm.beginDateFormated;
				
				vm.addArtistToEvent = new eventsService.addArtistToEvent();
				vm.modifyLocal = new eventsService.modifyLocalFromEvent();   
				vm.sendEvent = new eventsService.eventsP();  

				
				vm.selectedArtists = [];
				vm.selectedLocal = [];
				$scope.selected = [];									

				vm.eventId = $stateParams.id;

				vm.loadLocals = function () {
					localsService.locals.query({first: vm.firstLocal, max: (vm.maxLocal+1)}).$promise.then(function(data) {
						vm.locals = data;

						if(vm.firstLocal != 0) {
							vm.showPreviousButtonLocal = true;
						}
						else {
							vm.showPreviousButtonLocal = false;
						}

						if(vm.locals.length > 3) {
							vm.showNextButtonLocal = true;
							vm.locals.splice(vm.locals.length -1, 1);
						}
						else{
							vm.showNextButtonLocal = false;
						}
					});
				}	

				vm.loadLocalsKeywords = function () {
					localsService.localsByKeywords.query({ keywords: vm.keywordsLocal, first:vm.firstSearchLocal, max:vm.maxSearchLocal+1}).$promise.then(function(data) {
						vm.locals = data;
					
						if(vm.firstSearchLocal != 0) {
							vm.showPreviousButtonLocal = true;
						}
						else {
							vm.showPreviousButtonLocal = false;
						}

						if(vm.locals.length > 3) {
							vm.showNextButtonLocal = true;
							vm.locals.splice(vm.locals.length -1, 1);
						}
						else{
							vm.showNextButtonLocal = false;
						}
					});
				}

				vm.getNextLocal = function() {				
					if (vm.keywordsLocal && !(vm.keywordsLocal === "")){
						vm.firstSearchLocal += 3;
						vm.loadLocalsKeywords();
					}
					else {
						vm.firstLocal += 3;
						vm.loadLocals();
					}
				}

				vm.getPreviousLocal = function() {
					if (vm.keywordsLocal && !(vm.keywordsLocal === "")){
						vm.firstSearchLocal -= 3;
						if(vm.firstSearchLocal < 0){
							vm.firstSearchLocal = 0;
						}
						vm.loadLocalsKeywords();
					}
					else {
						vm.firstLocal -= 3;
						if(vm.firstLocal < 0){
							vm.firstLocal = 0;
						}
						vm.loadLocals();
					}

				}

				vm.searchLocals = function () {
					if (vm.keywordsLocal && !(vm.keywordsLocal === "")) {
						vm.loadLocalsKeywords();
					}
					else {
						vm.loadLocals();
					}
				};

				vm.loadArtists = function () {
					artistsService.artists.query({first: vm.firstArtist, max: (vm.maxArtist+1)}).$promise.then(function(data) {
						vm.artists = data;

						if(vm.firstArtist != 0) {
							vm.showPreviousButtonArtist = true;
						}
						else {
							vm.showPreviousButtonArtist = false;
						}

						if(vm.artists.length > 3) {
							vm.showNextButtonArtist = true;
							vm.artists.splice(vm.artists.length -1, 1);
						}
						else{
							vm.showNextButtonArtist = false;
						}
					});
				}	

				vm.loadArtistsKeywords = function () {
					artistsService.artistsByKeywords.query({ keywords: vm.keywordsArtist, first:vm.firstSearchArtist, max:vm.maxSearchArtist+1}).$promise.then(function(data) {
						vm.artists = data;
					
						if(vm.firstSearchArtist != 0) {
							vm.showPreviousButtonArtist = true;
						}
						else {
							vm.showPreviousButtonArtist = false;
						}

						if(vm.artists.length > 3) {
							vm.showNextButtonArtist = true;
							vm.artists.splice(vm.artists.length -1, 1);
						}
						else{
							vm.showNextButtonArtist = false;
						}
					});
				}

				vm.getNextArtist = function() {				
					if (vm.keywordsArtist && !(vm.keywordsArtist === "")){
						vm.firstSearchArtist += 3;
						vm.loadArtistsKeywords();
					}
					else {
						vm.firstArtist += 3;
						vm.loadArtists();
					}
				}

				vm.getPreviousArtist = function() {
					if (vm.keywordsArtist && !(vm.keywordsArtist === "")){
						vm.firstSearchArtist -= 3;
						if(vm.firstSearchArtist < 0){
							vm.firstSearchArtist = 0;
						}
						vm.loadArtistsKeywords();
					}
					else {
						vm.firstArtist -= 3;
						if(vm.firstArtist < 0){
							vm.firstArtist = 0;
						}
						vm.loadArtists();
					}

				}

				vm.searchArtists = function () {
					if (vm.keywordsArtist && !(vm.keywordsArtist === "")) {
						vm.loadArtistsKeywords();
					}
					else {
						vm.loadArtists();
					}
				};




				vm.reloadEventArtists = function () {
					artistsService.artistsByEvent.query({ id: vm.eventId, first: vm.firstEventArtists, max: vm.maxEventArtists+1 }).$promise.then(function(data) {
						vm.eventArtists = data;	
						artistsService.artistsByEvent.query({ id: vm.eventId, first: vm.firstEventArtists, max: vm.maxEventArtists+1 }).$promise.then(function(data) {
							vm.eventArtists = data;	
							artistsService.artistsByEvent.query({ id: vm.eventId, first: vm.firstEventArtists, max: vm.maxEventArtists+1 }).$promise.then(function(data) {
								vm.eventArtists = data;
								artistsService.artistsByEvent.query({ id: vm.eventId, first: 0, max: -1 }).$promise.then(function(data2) {
									vm.totalEventArtists = data2;

									vm.selectedArtists = [];	
									for (i = 0; i < vm.totalEventArtists.length; i++) {
										vm.selectedArtists.push(vm.totalEventArtists[i].id);
									}

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
        
                vm.updateEvent = function () {
					var inicio = vm.editEvent.beginDate;
					var inicioConv = inicio.toJSON();					
					//vm.editEvent.beginDate = inicioConv;					
					var fin = vm.editEvent.endDate;
					var finConv = fin.toJSON();					
					//vm.editEvent.endDate = finConv;

					vm.sendEvent.id = vm.editEvent.id;
					vm.sendEvent.name = vm.editEvent.name;
					vm.sendEvent.description = vm.editEvent.description;
					vm.sendEvent.beginDate = inicioConv;
					vm.sendEvent.endDate = finConv;

                    vm.sendEvent.$update({ id: vm.eventId })
						.then(function (result) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('¡Evento actualizado correctamente!')
									.position('top right')
									.hideDelay(3000)
								);
							//vm.reloadEvent();
							var params = { id: vm.eventId };
							$state.go('getEvent', params);
						}, function (error) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('Error: no ha podido actualizarse el evento.')
									.position('top right')
									.hideDelay(3000)
								);
							//vm.nuevoMovimiento.importe = -9999;
						});
				};

				function changeSelectedArtist (id)  {
					vm.selectedArtist = id; //TODO: esto se usa?
				}

				vm.addSelectedArtists = function () {
					//recorremos artistas guardados
					//por cada artista guardado, comprobamos que existe en los seleccionados.
					//en caso de no existir en los seleccionados, hay que borrar el artista guardado.
					var selected = false;
					for (i = 0; i < vm.totalEventArtists.length; i++) { 
						selected = false;
						for (j = 0; j < vm.selectedArtists.length; j++) {
							if (vm.totalEventArtists[i].id == vm.selectedArtists[j]){
								selected = true;
							}
						}
						if (!selected) {
							vm.deleteSelectedArtist(vm.totalEventArtists[i].id);
						}				
					}					

					var exists = false;
					for (i = 0; i < vm.selectedArtists.length; i++) { 
						exists = false;
						for (j = 0; j < vm.totalEventArtists.length; j++) {
							if (vm.totalEventArtists[j].id == vm.selectedArtists[i]){
								exists = true;
							}
						}
						if (!exists) {
							vm.addSelectedArtist(vm.selectedArtists[i]);
						}				
					}
				}

                vm.addSelectedArtist = function (selected) {
                   	vm.addArtistToEvent.$save({ eventId: vm.eventId, artistId: selected })
						.then(function (result) {
							vm.reloadEventArtists();							
							$mdToast.show(
								$mdToast.simple()
									.textContent('Artista añadido correctamente!')
									.position('top right')
									.hideDelay(3000)
								);
							//$state.reload();	
						}, function (error) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('Error añadiendo el artista.')
									.position('top right')
									.hideDelay(3000)
								);
							console.error(error);
							//vm.nuevoMovimiento.importe = -9999;
						});				
				};

				vm.modifySelectedLocal = function () {
					vm.modifyLocal.$update({ eventId: vm.eventId, localId: vm.selectedLocal[0]})
						.then(function (result) {
							vm.reloadEvent();
						}, function (error) {
							console.error(error);
							//vm.nuevoMovimiento.importe = -9999;
						});		
				};
				
                vm.deleteSelectedArtist = function (artistId) {					
                   	vm.addArtistToEvent.$delete({ eventId: vm.eventId, artistId: artistId })
						.then(function (result) {
							vm.reloadEventArtists();
							$mdToast.show(
								$mdToast.simple()
									.textContent('Artista eliminado correctamente!')
									.position('top right')
									.hideDelay(3000)
								);
							//$state.reload();	

							// cuando ha terminado el guardado del movimiento
							// es momento de pedir una actualización de datos
							//vm.nuevoMovimiento.importe = 0;
						}, function (error) {
							alert(error);
							$mdToast.show(
								$mdToast.simple()
									.textContent('Error eliminando el artista.')
									.position('top right')
									.hideDelay(3000)
								);
							console.error(error);
							//vm.nuevoMovimiento.importe = -9999;
						});				
				};

				vm.reloadEvent = function () {
					eventsService.events.get({ id: vm.eventId }).$promise.then(function(data) {
						vm.editEvent = data;
						vm.selectedLocal = [];
						vm.selectedLocal[0] = vm.editEvent.local.id;
						if (vm.editEvent.beginDate && !(vm.editEvent.beginDate instanceof Date)) {
							//var parseValue = self.dateLocale.parseDate(vm.editEvent.beginDate);
							vm.editEvent.beginDate = vm.editEvent.beginDate;
							var parseValue = new Date(vm.editEvent.beginDate);
							if (isNaN(parseValue)) {
								throw Error('The ng-model for md-datepicker must be a Date instance. ' +
								'Currently the model is a: ' + (typeof vm.editEvent.beginDate));
							} else {
								vm.editEvent.beginDate = parseValue;
								//vm.beginDateFormated = parseValue;
							}
						}
						if (vm.editEvent.endDate && !(vm.editEvent.endDate instanceof Date)) {
							//var parseValue = self.dateLocale.parseDate(vm.editEvent.beginDate);
							vm.editEvent.endDate = vm.editEvent.endDate;
							var parseValue = new Date(vm.editEvent.endDate);
							if (isNaN(parseValue)) {
								throw Error('The ng-model for md-datepicker must be a Date instance. ' +
								'Currently the model is a: ' + (typeof vm.editEvent.endDate));
							} else {
								vm.editEvent.endDate = parseValue;
								//vm.beginDateFormated = parseValue;
							}
						}

					});
				}


				// vm.showPrompt = function(ev) {
				// 	// // Appending dialog to document.body to cover sidenav in docs app
				// 	// var confirm = $mdDialog.prompt()
				// 	// .title('What would you name your dog?')
				// 	// .textContent('Bowser is a common name.')
				// 	// .placeholder('Dog name')
				// 	// .ariaLabel('Dog name')
				// 	// .initialValue('Buddy')
				// 	// .targetEvent(ev)
				// 	// .ok('Okay!')
				// 	// .cancel('I\'m a cat person');

				// 	// $mdDialog.show(confirm).then(function(result) {
				// 	// $scope.status = 'You decided to name your dog ' + result + '.';
				// 	// }, function() {
				// 	// $scope.status = 'You didn\'t name your dog.';
				// 	// });
				// 	$mdDialog.show({
				// 		clickOutsideToClose: true,

				// 		scope: this,        // use parent scope in template
				// 		preserveScope: true,  // do not forget this if use parent scope

				// 		// Since GreetingController is instantiated with ControllerAs syntax
				// 		// AND we are passing the parent '$scope' to the dialog, we MUST
				// 		// use 'vm.<xxx>' in the template markup

				// 		template: '<md-dialog>' +
				// 					'  <md-dialog-content>' +
				// 					'     Hi There {{vm.eventId}}' +
				// 					'  </md-dialog-content>' +
				// 					'</md-dialog>',

				// 		controller: function DialogController($scope, $mdDialog) {
				// 			$scope.closeDialog = function() {
				// 			$mdDialog.hide();
				// 			}
				// 		}
				// 	});
				// };

				// function showCustomGreeting(ev) {

				// 	$mdDialog.show({
				// 		clickOutsideToClose: true,

				// 		scope: $scope,        // use parent scope in template
				// 		preserveScope: true,  // do not forget this if use parent scope

				// 		// Since GreetingController is instantiated with ControllerAs syntax
				// 		// AND we are passing the parent '$scope' to the dialog, we MUST
				// 		// use 'vm.<xxx>' in the template markup

				// 		template: '<md-dialog>' +
				// 					'  <md-dialog-content>' +
				// 					'     Hi There {{vm.eventId}}' +
				// 					'  </md-dialog-content>' +
				// 					'</md-dialog>',

				// 		controller: function DialogController($scope, $mdDialog) {
				// 			$scope.closeDialog = function() {
				// 			$mdDialog.hide();
				// 			}
				// 		}
				// 	});
				// 	};

					vm.greeting = 'Hello';
					vm.hideDialog = $mdDialog.hide;
					//vm.showDialog = vm.showDialog;

					vm.showDialog = function(evt) {
						vm.dialogOpen = true;
						$mdDialog.show({
						targetEvent: evt,
						controller: function () { 
							this.parent = vm; 
							},
						controllerAs: 'ctrl',
						templateUrl: './states/event/editArtistsDialog.html'
							// '<md-dialog>' +
							// '  <md-content>{{ctrl.parent.greeting}}, world !</md-content>' +
							// '  <div class="md-actions">' +
							// '    <md-button ng-click="ctrl.parent.dialogOpen=false;ctrl.parent.hideDialog()">' +
							// '      Close' +
							// '    </md-button>' +
							// '  </div>' +
							// '</md-dialog>'
						});
					}

					vm.modifyLocalDialog = function(evt) {
					vm.dialogOpen = true;
					$mdDialog.show({
						targetEvent: evt,
						controller: function () { 
							this.parent = vm; 
							},
						controllerAs: 'ctrl',
						templateUrl: './states/event/editLocalDialog.html'
							// '<md-dialog>' +
							// '  <md-content>{{ctrl.parent.greeting}}, world !</md-content>' +
							// '  <div class="md-actions">' +
							// '    <md-button ng-click="ctrl.parent.dialogOpen=false;ctrl.parent.hideDialog()">' +
							// '      Close' +
							// '    </md-button>' +
							// '  </div>' +
							// '</md-dialog>'
						});
					}

					vm.$onInit = function() {
						//vm.editEvent = eventsService.events.get({ id: vm.eventId });	

						vm.reloadEvent();



						//paso de cargar fechas en string a una variable aparte en formato Date

						vm.reloadEventArtists();
						vm.loadArtists();
						vm.loadLocals();
					};
			}
		})

}());