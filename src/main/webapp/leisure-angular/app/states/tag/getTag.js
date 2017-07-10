(function () {

	angular.module('getTag', ['services'])
		.config(function ($stateProvider) {
			$stateProvider
				.state('getTag', {
					url: '/getTag/:id',
					template: '<get-tag></get-tag>'
				})
		})
		.component('getTag', {
			templateUrl: './states/tag/getTag.html',
			controller: function (tagsService, usersService, eventsService, $state, $stateParams) {
                var vm = this;
               	vm.loginInfo = [];

				usersService.loginInfo.query().$promise.then(function(data) {
					vm.loginInfo = data;
				});
				vm.delete = new tagsService.tagsP();  

                var tagId = $stateParams.id;
                vm.tag = tagsService.tags.get({ id: tagId });
				eventsService.eventsByTag.query({ id: tagId }).$promise.then(function(data) {
					vm.events = data;
				});

				vm.deleteTag = function(){
					vm.delete.$delete({ id: tagId })
						.then(function (result) {
							$state.go('getAllTags');
						}, function (error) {
							console.error(error);
						});
				}
      
	  			vm.eventDetails = function (id) {
					var params = { id: id };
					$state.go('getEvent', params);
				}
			}
		})
}());