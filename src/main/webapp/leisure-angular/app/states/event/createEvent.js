(function () {

	angular.module('createEvent', ['ui.router','services'])
		.config(function ($stateProvider) {
			$stateProvider
				.state('createEvent', {
					url: '/createEvent',
					template: '<create-event></create-event>'
				})
		})
		.component('createEvent', {
			templateUrl: './states/event/createEvent.html',
			controller: function (eventsService, localsService, $mdDialog, $state) {
                var vm = this;
				vm.keywords = "";
				vm.first = 0;
				vm.max = 3;
				vm.firstSearch = 0;
				vm.maxSearch = 3;
				vm.showNextButton = false;
				vm.showPreviousButton = false;

                vm.selectedLocal = [];
                //vm.newEvent = new eventsService.events();      
				vm.newEvent = new eventsService.createEvent();             
                vm.myDate;

				vm.loadLocals = function () {
					localsService.locals.query({first: vm.first, max: (vm.max+1)}).$promise.then(function(data) {
						vm.locals = data;

						if(vm.first != 0) {
							vm.showPreviousButton = true;
						}
						else {
							vm.showPreviousButton = false;
						}

						if(vm.locals.length > 3) {
							vm.showNextButton = true;
							vm.locals.splice(vm.locals.length -1, 1);
						}
						else{
							vm.showNextButton = false;
						}
					});
				}	

				vm.loadLocalsKeywords = function () {
					localsService.localsByKeywords.query({ keywords: vm.keywords, first:vm.firstSearch, max:vm.maxSearch+1}).$promise.then(function(data) {
						vm.locals = data;
					
						if(vm.firstSearch != 0) {
							vm.showPreviousButton = true;
						}
						else {
							vm.showPreviousButton = false;
						}

						if(vm.locals.length > 3) {
							vm.showNextButton = true;
							vm.locals.splice(vm.locals.length -1, 1);
						}
						else{
							vm.showNextButton = false;
						}
					});
				}

				vm.getNext = function() {				
					if (vm.keywords && !(vm.keywords === "")){
						vm.firstSearch += 3;
						vm.loadLocalsKeywords();
					}
					else {
						vm.first += 3;
						vm.loadLocals();
					}
				}

				vm.getPrevious = function() {
					if (vm.keywords && !(vm.keywords === "")){
						vm.firstSearch -= 3;
						if(vm.firstSearch < 0){
							vm.firstSearch = 0;
						}
						vm.loadLocalsKeywords();
					}
					else {
						vm.first -= 3;
						if(vm.first < 0){
							vm.first = 0;
						}
						vm.loadLocals();
					}

				}

				vm.searchLocals = function () {
					if (vm.keywords && !(vm.keywords === "")) {
						vm.loadLocalsKeywords();
					}
					else {
						vm.loadLocals();
					}
				};

                vm.createEvent = function () {           
					vm.newEvent.$save({ localId: vm.selectedLocal[0]})
						.then(function (result) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('Evento creado correctamente!')
									.position('top right')
									.hideDelay(3000)
								);
							$state.go('getAllEvents');
						}, function (error) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('Error: el evento no ha podido crearse.')
									.position('top right')
									.hideDelay(4000)
								);
						});
				};


				// vm.addSelectedLocal = function () {
				// 	//en vm.selectedLocal tendremos el id del local a añadir.
				// 	//hacermos un get local por id, y cuando la promesa esté resuelta, 
				// 	//lo chantamos dentro del objeto vm.newEvent, vm.newEvent.local = ...
				// 	if (vm.selectedLocal != -1) {
				// 		vm.local = localsService.locals.get({ id: vm.selectedLocal });
				// 		vm.newEvent.local = vm.local;
				// 	}
				// 	else {
				// 		alert("Aqui no hay local ninguno! y pon un mdToast cutron!");
				// 	}			
				// };

				vm.hideDialog = $mdDialog.hide;
				vm.selectLocalDialog = vm.selectLocalDialog;
				vm.selectLocalDialog = function(evt) {
					vm.dialogOpen = true;
					$mdDialog.show({
					targetEvent: evt,
					controller: function () { 
						this.parent = vm; 
						},
					controllerAs: 'ctrl',
					templateUrl: './states/event/selecLocalDialog.html'
					});
				};

				vm.logItem = function (item) {
   					console.log(item.name, 'was selected');
  				};

				vm.license = {
        			expirationdate: '2015-12-15T23:00:00.000Z'
    			};

    			vm.dt = new Date(vm.license.expirationdate);

				vm.$onInit = function() {
					vm.loadLocals();
					// vm.eventId = $stateParams.id;
					// vm.editEvent = eventsService.events.get({ id: vm.eventId });			
					// vm.reloadEventArtists();
					// vm.reloadAllArtists();
					// vm.reloadAllLocals();
				};
			}
		})
}());