(function () {

	angular.module('createTag', ['ui.router','services'])
		.config(function ($stateProvider) {
			$stateProvider
				.state('createTag', {
					url: '/createTag',
					template: '<create-tag></create-tag>'
				})
		})
		.component('createTag', {
			templateUrl: './states/tag/createTag.html',
			controller: function ($mdToast, $state, tagsService) {
                var vm = this;
                vm.newTag = new tagsService.tagsP();             
				vm.createTag = function () {     
					vm.newTag.$save()
						.then(function (result) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('¡Tag creado correctamente!')
									.position('top right')
									.hideDelay(3000)
								);
							$state.go('getAllTags');
						}, function (error) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('Error: el tag no ha podido crearse.')
									.position('top right')
									.hideDelay(4000)
								);
						});
				};
			}
		})
}());