(function () {

	angular.module('getLocal', ['services'])
		.config(function ($stateProvider) {
			$stateProvider
				.state('getLocal', {
					url: '/getLocal/:id',
					template: '<get-local></get-local>'
				})
		})
		.component('getLocal', {
			templateUrl: './states/local/getLocal.html',
			controller: function (localsService, usersService, ratingsService, eventsService, $state, $stateParams) {
                var vm = this;
				vm.loginInfo = [];
				vm.eventsLocal = [];
				usersService.loginInfo.query().$promise.then(function(data) {
					vm.loginInfo = data;
				});

                vm.localId = $stateParams.id;
				vm.delete = new localsService.localsP();  

                //vm.local = localsService.locals.get({ id: localId });
				localsService.locals.get({ id: vm.localId }).$promise.then(function(data) {
					vm.local = data;
					eventsService.eventsByLocal.query({ id: vm.localId }).$promise.then(function(data2) {
						vm.eventsLocal = data2;	
					});
				});

				vm.deleteLocal = function(){
					vm.delete.$delete({ id: vm.localId })
						.then(function (result) {
							$state.go('getAllLocals');
						}, function (error) {
							console.error(error);
						});
				}

				vm.eventDetails = function (id) {
					var params = { id: id };
					$state.go('getEvent', params);
				}

				vm.loadRating = function () {
					ratingsService.globalRatingLocal.query({ id: vm.localId }).$promise.then(function(data) {
						vm.globalRating = data;
					});
				};

				vm.loadRating();

      
			}
		})

}());