(function () {

	//angular.module('test', ['ui.router','ngMaterial','ngAnimate','ngAria', 'ngGoogleMaps'])
	angular.module('test', ['ui.router','ngMaterial','ngAnimate','ngAria'])
		.config(function ($stateProvider) {
			$stateProvider
				.state('test', {
					url: '/test',
					template: '<test></test>'
				})
		})
		.component('test', {
			templateUrl: './states/test/test.html',
			controller: function (localsService, NgMap) {
                var vm = this;
                localsService.locals.query().$promise.then(function(data) {
					vm.locals = data;
				});
				// NgMap.getMap().then(function(map) {
				// console.log(map.getCenter());
				// console.log('markers', map.markers);
				// console.log('shapes', map.shapes);
  				// });

				//$scope.map = {center: {latitude: 51.219053, longitude: 4.404418 }, zoom: 14 };

			}
		})

}());