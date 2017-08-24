(function () {

	angular.module('getAllTags', ['ui.router','services', 'ngMaterial','ngAnimate','ngAria'])
		.config(function ($stateProvider) {
			$stateProvider
				.state('getAllTags', {
					url: '/getAllTags',
					template: '<get-all-tags></get-all-tags>'
				})
		})
		.component('getAllTags', {
			templateUrl: './states/tag/getAllTags.html',
			controller: function (tagsService, usersService, $state) {
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

				vm.loadTags = function () {
					tagsService.tags.query({first: vm.first, max: (vm.max+1)}).$promise.then(function(data) {
						vm.tags = data;

						if(vm.first != 0) {
							vm.showPreviousButton = true;
						}
						else {
							vm.showPreviousButton = false;
						}

						if(vm.tags.length > 5) {
							vm.showNextButton = true;
							vm.tags.splice(vm.tags.length -1, 1);
						}
						else{
							vm.showNextButton = false;
						}
					});
				};

				vm.loadTagsKeywords = function () {
					tagsService.tagsByKeywords.query({ keywords: vm.keywords, first:vm.firstSearch, max:vm.maxSearch+1}).$promise.then(function(data) {
						vm.tags = data;
					
						if(vm.firstSearch != 0) {
							vm.showPreviousButton = true;
						}
						else {
							vm.showPreviousButton = false;
						}

						if(vm.tags.length > 5) {
							vm.showNextButton = true;
							vm.tags.splice(vm.tags.length -1, 1);
						}
						else{
							vm.showNextButton = false;
						}
					});
				};	

				vm.getNext = function() {				
					if (vm.keywords && !(vm.keywords === "")){
						vm.firstSearch += 5;
						vm.loadTagsKeywords();
					}
					else {
						vm.first += 5;
						vm.loadTags();
					}
				};

				vm.getPrevious = function() {
					if (vm.keywords && !(vm.keywords === "")){
						vm.firstSearch -= 5;
						if(vm.firstSearch < 0){
							vm.firstSearch = 0;
						}
						vm.loadTagsKeywords();
					}
					else {
						vm.first -= 5;
						if(vm.first < 0){
							vm.first = 0;
						}
						vm.loadTags();
					}
				};

				vm.searchTags = function () {
					if (vm.keywords && !(vm.keywords === "")) {
						vm.loadTagsKeywords();
					}
					else {
						vm.loadTags();
					}
				};

				vm.tagDetails = function (id) {
					var params = { id: id };
					$state.go('getTag', params);
				};

				vm.loadTags();
			}
		});

}());