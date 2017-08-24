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
						for (i=0;i<vm.eventsLocal.length;i++){
							vm.eventsLocal[i].beginDate = new Date(vm.eventsLocal[i].beginDate);
						}	
					});
				});

				vm.deleteLocal = function(){
					vm.delete.$delete({ id: vm.localId })
						.then(function (result) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('¡Local eliminado correctamente!')
									.position('top right')
									.hideDelay(3000)
								);
							$state.go('getAllLocals');
						}, function (error) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('Error: no ha podido eliminarse el local.')
									.position('top right')
									.hideDelay(4000)
								);
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