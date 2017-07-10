(function () {

	angular.module('getAllUsers', ['ui.router','services', 'ngMaterial','ngAnimate','ngAria'])
		.config(function ($stateProvider) {
			$stateProvider
				.state('getAllUsers', {
					url: '/getAllUsers',
					template: '<get-all-users></get-all-users>'
				})
		})
		.component('getAllUsers', {
			templateUrl: './states/user/getAllUsers.html',
			controller: function (usersService,$state) {
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

				vm.loadUsers = function () {
					usersService.users.query({first: vm.first, max: (vm.max+1)}).$promise.then(function(data) {
						vm.users = data;

						if(vm.first != 0) {
							vm.showPreviousButton = true;
						}
						else {
							vm.showPreviousButton = false;
						}

						if(vm.users.length > 5) {
							vm.showNextButton = true;
							vm.users.splice(vm.users.length -1, 1);
						}
						else{
							vm.showNextButton = false;
						}
					});
				}	

				vm.loadUsersKeywords = function () {
					usersService.usersByKeywords.query({ keywords: vm.keywords, first:vm.firstSearch, max:vm.maxSearch+1}).$promise.then(function(data) {
						vm.users = data;
					
						if(vm.firstSearch != 0) {
							vm.showPreviousButton = true;
						}
						else {
							vm.showPreviousButton = false;
						}

						if(vm.users.length > 5) {
							vm.showNextButton = true;
							vm.users.splice(vm.users.length -1, 1);
						}
						else{
							vm.showNextButton = false;
						}
					});
				}

				vm.getNext = function() {				
					if (vm.keywords && !(vm.keywords === "")){
						vm.firstSearch += 5;
						vm.loadUsersKeywords();
					}
					else {
						vm.first += 5;
						vm.loadUsers();
					}
				}

				vm.getPrevious = function() {
					if (vm.keywords && !(vm.keywords === "")){
						vm.firstSearch -= 5;
						if(vm.firstSearch < 0){
							vm.firstSearch = 0;
						}
						vm.loadUsersKeywords();
					}
					else {
						vm.first -= 5;
						if(vm.first < 0){
							vm.first = 0;
						}
						vm.loadUsers();
					}
				}

				vm.searchUsers = function () {
					if (vm.keywords && !(vm.keywords === "")) {
						vm.loadUsersKeywords();
					}
					else {
						vm.loadUsers();
					}
				};

				vm.userDetails = function (id) {
					var params = { id: id };
					$state.go('getUser', params);
				};

				vm.loadUsers();
			}
		})

}());