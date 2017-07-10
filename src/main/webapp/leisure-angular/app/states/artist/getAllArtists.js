(function () {

	angular.module('getAllArtists', ['ui.router','services', 'ngMaterial','ngAnimate','ngAria'])
		.config(function ($stateProvider) {
			$stateProvider
				.state('getAllArtists', {
					url: '/getAllArtists',
					template: '<get-all-artists></get-all-artists>'
				})
		})
		.component('getAllArtists', {
			templateUrl: './states/artist/getAllArtists.html',
			controller: function (artistsService, usersService, $state) {
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

				vm.loadArtists = function () {
					artistsService.artists.query({first: vm.first, max: (vm.max+1)}).$promise.then(function(data) {
						vm.artists = data;

						if(vm.first != 0) {
							vm.showPreviousButton = true;
						}
						else {
							vm.showPreviousButton = false;
						}

						if(vm.artists.length > 5) {
							vm.showNextButton = true;
							vm.artists.splice(vm.artists.length -1, 1);
						}
						else{
							vm.showNextButton = false;
						}
					});
				}	

				vm.loadArtistsKeywords = function () {
					artistsService.artistsByKeywords.query({ keywords: vm.keywords, first:vm.firstSearch, max:vm.maxSearch+1}).$promise.then(function(data) {
						vm.artists = data;
					
						if(vm.firstSearch != 0) {
							vm.showPreviousButton = true;
						}
						else {
							vm.showPreviousButton = false;
						}

						if(vm.artists.length > 5) {
							vm.showNextButton = true;
							vm.artists.splice(vm.artists.length -1, 1);
						}
						else{
							vm.showNextButton = false;
						}
					});
				}

				vm.getNext = function() {				
					if (vm.keywords && !(vm.keywords === "")){
						vm.firstSearch += 5;
						vm.loadArtistsKeywords();
					}
					else {
						vm.first += 5;
						vm.loadArtists();
					}
				}

				vm.getPrevious = function() {
					if (vm.keywords && !(vm.keywords === "")){
						vm.firstSearch -= 5;
						if(vm.firstSearch < 0){
							vm.firstSearch = 0;
						}
						vm.loadArtistsKeywords();
					}
					else {
						vm.first -= 5;
						if(vm.first < 0){
							vm.first = 0;
						}
						vm.loadArtists();
					}

				}

				vm.searchArtists = function () {
					if (vm.keywords && !(vm.keywords === "")) {
						vm.loadArtistsKeywords();
					}
					else {
						vm.loadArtists();
					}
				};

				vm.artistDetails = function (id) {
					var params = { id: id };
					$state.go('getArtist', params);
				};

				vm.loadArtists();

			}
		})

}());