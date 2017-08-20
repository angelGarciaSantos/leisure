(function () {
	angular.module('menu', ['ui.router','services', 'ngMaterial','ngAnimate','ngAria'])
		.component('menu', {
			templateUrl: './components/menu/menu.html',
			controller: function ($timeout, $window, $mdToast, $route, $mdDialog, $state, $mdSidenav, usersService) {
                var vm = this;
                vm.hideDialog = $mdDialog.hide;
				vm.userLogin = new usersService.login();             
                vm.login = function () {
					//vm.nuevoMovimiento.fecha = new Date(vm.nuevoMovimiento.fecha);              
					vm.userLogin.$save()
						.then(function (result) {
							usersService.loginInfo.query().$promise.then(function(data) {
								vm.loginInfo = data;
							});
							$mdToast.show(
								$mdToast.simple()
									.textContent('¡Login correcto!')
									.position('top right')
									.hideDelay(3000)
								);
							$window.location.reload();
						}, function (error) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('Error: no se pudo hacer login.')
									.position('top right')
									.hideDelay(4000)
								);
						});
				};

				vm.userRegister = new usersService.register();             
                vm.register = function () {
                    if (vm.userRegister.password != vm.passwordCheck) {
                        $mdToast.show(
                            $mdToast.simple()
                                .textContent('Error: las contraseñas no coinciden.')
                                .position('top right')
                                .hideDelay(4000)
                            );
                    }
                    else {
                        vm.userRegister.$save()
                            .then(function (result) {
                                $mdToast.show(
                                    $mdToast.simple()
                                        .textContent('¡Registro correcto!')
                                        .position('top right')
                                        .hideDelay(3000)
                                    );
								$window.location.reload();
                            }, function (error) {
                                $mdToast.show(
                                    $mdToast.simple()
                                        .textContent('Error: no pudo registrarse.')
                                        .position('top right')
                                        .hideDelay(4000)
                                    );
                            });

                    }
				};

				vm.editLoginDialog = function (evt) {
					usersService.editLogin.get({id: vm.loginInfo[0]}).$promise.then(function(data) {
						vm.editLogin = data;
						vm.editLogin.password = "";
						vm.dialogOpen = true;
						$mdDialog.show({
							targetEvent: evt,
							controller: function () { 
								this.parent = vm; 
							},
							controllerAs: 'ctrl',
							templateUrl: './components/menu/editLoginDialog.html'
						});



					});       




				};
			
				vm.userLogout = new usersService.logout(); 
				vm.loginInfo = [];
				usersService.loginInfo.query().$promise.then(function(data) {
					vm.loginInfo = data;
				});


				vm.toggleLeft = buildToggler('left');
				vm.toggleRight = buildToggler('right');

				 function buildToggler (componentId) {
					return function() {
						$mdSidenav(componentId).toggle();
					};
				};

				vm.loginDialog = function(evt) {
					vm.dialogOpen = true;
					$mdDialog.show({
						targetEvent: evt,
						controller: function () { 
							this.parent = vm; 
						},
						controllerAs: 'ctrl',
						templateUrl: './components/menu/loginDialog.html'
					});
				};

				vm.registerDialog = function(evt) {
					vm.dialogOpen = true;
					$mdDialog.show({
						targetEvent: evt,
						controller: function () { 
							this.parent = vm; 
						},
						controllerAs: 'ctrl',
						templateUrl: './components/menu/registerUserDialog.html'
					});
				};

				vm.logout = function () {
					//vm.nuevoMovimiento.fecha = new Date(vm.nuevoMovimiento.fecha);              
					vm.userLogout.$delete()
						.then(function (result) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('¡Logout correcto!')
									.position('top right')
									.hideDelay(3000)
								);
								usersService.loginInfo.query().$promise.then(function(data) {
									vm.loginInfo = data;
									vm.recommendedArtists = [];	
								});
								//$window.location.reload();
								$state.go('home');
								$window.location.reload();
						}, function (error) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('Error: no se pudo hacer logout.')
									.position('top right')
									.hideDelay(4000)
								);
						});
				};

				vm.editUserLogin = function () {
                    if (vm.editLogin.password != vm.editPasswordCheck) {
                        $mdToast.show(
                            $mdToast.simple()
                                .textContent('Error: las contraseñas no coinciden.')
                                .position('top right')
                                .hideDelay(4000)
                            );
                    }
                    else {
                        vm.editLogin.$update({ id: vm.loginInfo[0] })
                            .then(function (result) {
                                $mdToast.show(
                                    $mdToast.simple()
                                        .textContent('¡Perfil actualizado correctamente!')
                                        .position('top right')
                                        .hideDelay(3000)
                                    );
								$window.location.reload();
                            }, function (error) {
                                $mdToast.show(
                                    $mdToast.simple()
                                        .textContent('Error: el perfil no pudo actualizarse.')
                                        .position('top right')
                                        .hideDelay(4000)
                                    );
                            });

                    }
				};            
			}
		})

}());