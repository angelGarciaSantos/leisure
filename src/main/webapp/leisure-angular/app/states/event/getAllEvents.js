(function () {

	angular.module('getAllEvents', ['ui.router','services', 'ngMaterial','ngAnimate','ngAria'])
		.config(function ($stateProvider) {
			$stateProvider
				.state('getAllEvents', {
					url: '/getAllEvents',
					template: '<get-all-events></get-all-events>'
				})
		})
		.component('getAllEvents', {
			templateUrl: './states/event/getAllEvents.html',
			controller: function (eventsService, usersService, $state) {
                var vm = this;
               vm.keywords = "";
				vm.first = 0;
				vm.max = 5;
				vm.firstSearch = 0;
				vm.maxSearch = 5;

				vm.loginInfo = [];
				vm.showNextButton = false;
				vm.showPreviousButton = false;

				usersService.loginInfo.query().$promise.then(function(data) {
					vm.loginInfo = data;
				});

				vm.loadEvents = function () {
					eventsService.events.query({first: vm.first, max: (vm.max+1)}).$promise.then(function(data) {
						vm.events = data;

						if(vm.first != 0) {
							vm.showPreviousButton = true;
						}
						else {
							vm.showPreviousButton = false;
						}

						if(vm.events.length > 5) {
							vm.showNextButton = true;
							vm.events.splice(vm.events.length -1, 1);
						}
						else{
							vm.showNextButton = false;
						}

						for (i=0;i<vm.events.length;i++){
							vm.events[i].beginDate = new Date(vm.events[i].beginDate);
						}
					});
				}	

				vm.loadEventsKeywords = function () {
					eventsService.eventsByKeywords.query({ keywords: vm.keywords, first:vm.firstSearch, max:vm.maxSearch+1}).$promise.then(function(data) {
						vm.events = data;
					
						if(vm.firstSearch != 0) {
							vm.showPreviousButton = true;
						}
						else {
							vm.showPreviousButton = false;
						}

						if(vm.events.length > 5) {
							vm.showNextButton = true;
							vm.events.splice(vm.events.length -1, 1);
						}
						else{
							vm.showNextButton = false;
						}
						for (i=0;i<vm.events.length;i++){
							vm.events[i].beginDate = new Date(vm.events[i].beginDate);
						}
					});
				}

				vm.getNext = function() {				
					if (vm.keywords && !(vm.keywords === "")){
						vm.firstSearch += 5;
						vm.loadEventsKeywords();
					}
					else {
						vm.first += 5;
						vm.loadEvents();
					}
				}

				vm.getPrevious = function() {
					if (vm.keywords && !(vm.keywords === "")){
						vm.firstSearch -= 5;
						if(vm.firstSearch < 0){
							vm.firstSearch = 0;
						}
						vm.loadEventsKeywords();
					}
					else {
						vm.first -= 5;
						if(vm.first < 0){
							vm.first = 0;
						}
						vm.loadEvents();
					}

				}

				vm.searchEvents = function () {
					if (vm.keywords && !(vm.keywords === "")) {
						vm.loadEventsKeywords();
					}
					else {
						vm.loadEvents();
					}
				};

				vm.eventDetails = function (id) {
					var params = { id: id };
					$state.go('getEvent', params);
				};


				vm.$onInit = function() {
					vm.loadEvents();
				};


			}
		})

}());