(function () {

	angular.module('updateTag', ['ui.router', 'services'])
		.config(function ($stateProvider) {
			$stateProvider
				.state('updateTag', {
					url: '/updateTag/:id',
					template: '<update-tag></update-tag>'
				})
		})
		.component('updateTag', {
			templateUrl: './states/tag/updateTag.html',
			controller: function (tagsService, $state, $stateParams,$mdToast) {
                var vm = this;

                var tagId = $stateParams.id;
                vm.editTag = tagsService.tags.get({ id: tagId });
				vm.sendTag = new tagsService.tagsP();  


                vm.updateTag = function () {
					vm.sendTag.id = vm.editTag.id;
					vm.sendTag.name = vm.editTag.name;
                    vm.sendTag.$update({ id: tagId })
						.then(function (result) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('¡Tag actualizado correctamente!')
									.position('top right')
									.hideDelay(3000)
								);
							$state.go('getAllTags');
						}, function (error) {
							console.error(error);
								$mdToast.show(
									$mdToast.simple()
										.textContent('Error: el Tag no ha podido actualizarse.')
										.position('top right')
										.hideDelay(4000)
									);						
								});
				};
			}
		})

}());