(function () {

	angular.module('getAllLocals', ['ui.router','services', 'ngMaterial','ngAnimate','ngAria'])
		.config(function ($stateProvider) {
			$stateProvider
				.state('getAllLocals', {
					url: '/getAllLocals',
					template: '<get-all-locals></get-all-locals>'
				})
		})
		.component('getAllLocals', {
			templateUrl: './states/local/getAllLocals.html',
			controller: function (localsService, usersService, $state) {
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

				vm.loadLocals = function () {
					localsService.locals.query({first: vm.first, max: (vm.max+1)}).$promise.then(function(data) {
						vm.locals = data;

						if(vm.first != 0) {
							vm.showPreviousButton = true;
						}
						else {
							vm.showPreviousButton = false;
						}

						if(vm.locals.length > 5) {
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

						if(vm.locals.length > 5) {
							vm.showNextButton = true;
							vm.locals.splice(vm.artists.length -1, 1);
						}
						else{
							vm.showNextButton = false;
						}
					});
				}

				vm.getNext = function() {				
					if (vm.keywords && !(vm.keywords === "")){
						vm.firstSearch += 5;
						vm.loadLocalsKeywords();
					}
					else {
						vm.first += 5;
						vm.loadLocals();
					}
				}

				vm.getPrevious = function() {
					if (vm.keywords && !(vm.keywords === "")){
						vm.firstSearch -= 5;
						if(vm.firstSearch < 0){
							vm.firstSearch = 0;
						}
						vm.loadLocalsKeywords();
					}
					else {
						vm.first -= 5;
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

				vm.localDetails = function (id) {
					var params = { id: id };
					$state.go('getLocal', params);
				}

				vm.loadLocals();
			}
		})

}());