(function () {

	angular.module('getUser', ['services'])
		.config(function ($stateProvider) {
			$stateProvider
				.state('getUser', {
					url: '/getUser/:id',
					template: '<get-user></get-user>'
				})
		})
		.component('getUser', {
			templateUrl: './states/user/getUser.html',
			controller: function (usersService, $state, $stateParams) {
                var vm = this;
				vm.loginInfo = [];

				usersService.loginInfo.query().$promise.then(function(data) {
					vm.loginInfo = data;
				});

                vm.userId = $stateParams.id;
                vm.user = usersService.users.get({ id: vm.userId });

				vm.deleteUser = function() {
					vm.user.$delete({ id: vm.userId })
						.then(function (result) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('¡Usuario eliminado correctamente!')
									.position('top right')
									.hideDelay(3000)
								);
							$state.go("getAllUsers");
						}, function (error) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('Error: el usuario no ha podido eliminarse.')
									.position('top right')
									.hideDelay(4000)
								);
						});
				}

			}
		})

}());