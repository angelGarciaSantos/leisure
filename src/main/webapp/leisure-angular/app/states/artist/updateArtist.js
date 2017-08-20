(function () {

	angular.module('updateArtist', ['ui.router', 'services'])
		.config(function ($stateProvider) {
			$stateProvider
				.state('updateArtist', {
					url: '/updateArtist/:id',
					template: '<update-artist></update-artist>'
				})
		})
		.component('updateArtist', {
			templateUrl: './states/artist/updateArtist.html',
			controller: function ($mdToast, artistsService, tagsService, $mdDialog, $state, $stateParams) {
                var vm = this;

				var artistId = $stateParams.id;
				vm.artistId = $stateParams.id;
                vm.editArtist = artistsService.artists.get({ id: artistId });
				vm.sendArtist = new artistsService.artistsP();  

				vm.firstArtistTags = 0;
				vm.maxArtistTags = 3;
				vm.showNextButtonArtistTag = false;
				vm.showPreviousButtonArtistTag = false;
				vm.keywordsTag = "";
				vm.firstTag = 0;
				vm.maxTag = 3;
				vm.firstSearchTag = 0;
				vm.maxSearchTag = 3;
				vm.showNextButtonTag = false;
				vm.showPreviousButtonTag = false;
				vm.selectedTags = [];
				vm.addTagToArtist = new tagsService.addTagToArtist();

				vm.loadTags = function () {
					tagsService.tags.query({first: vm.firstTag, max: (vm.maxTag+1)}).$promise.then(function(data) {
						vm.tags = data;

						if(vm.firstTag != 0) {
							vm.showPreviousButtonTag = true;
						}
						else {
							vm.showPreviousButtonTag = false;
						}

						if(vm.tags.length > 3) {
							vm.showNextButtonTag = true;
							vm.tags.splice(vm.tags.length -1, 1);
						}
						else{
							vm.showNextButtonTag = false;
						}
					});
				}	

				vm.loadTagsKeywords = function () {
					tagsService.tagsByKeywords.query({ keywords: vm.keywordsTag, first:vm.firstSearchTag, max:vm.maxSearchTag+1}).$promise.then(function(data) {
						vm.tags = data;
					
						if(vm.firstSearchTag != 0) {
							vm.showPreviousButtonTag = true;
						}
						else {
							vm.showPreviousButtonTag = false;
						}

						if(vm.tags.length > 3) {
							vm.showNextButtonTag = true;
							vm.tags.splice(vm.tags.length -1, 1);
						}
						else{
							vm.showNextButtonTag = false;
						}
					});
				}

				vm.getNextTag = function() {				
					if (vm.keywordsTag && !(vm.keywordsTag === "")){
						vm.firstSearchTag += 3;
						vm.loadTagsKeywords();
					}
					else {
						vm.firstTag += 3;
						vm.loadTags();
					}
				}

				vm.getPreviousTag = function() {
					if (vm.keywordsTag && !(vm.keywordsTag === "")){
						vm.firstSearchTag -= 3;
						if(vm.firstSearchTag < 0){
							vm.firstSearchTag = 0;
						}
						vm.loadTagsKeywords();
					}
					else {
						vm.firstTag -= 3;
						if(vm.firstTag < 0){
							vm.firstTag = 0;
						}
						vm.loadTags();
					}

				}

				vm.searchTags = function () {
					if (vm.keywordsTag && !(vm.keywordsTag === "")) {
						vm.loadTagsKeywords();
					}
					else {
						vm.loadTags();
					}
				};




				vm.reloadArtistTags = function () {
					tagsService.tagsByArtist.query({ id: vm.artistId, first: vm.firstArtistTags, max: vm.maxArtistTags+1 }).$promise.then(function(data) {
						vm.artistTags = data;				
						tagsService.tagsByArtist.query({ id: vm.artistId, first: 0, max: -1 }).$promise.then(function(data2) {
							vm.totalArtistTags = data2;
							vm.selectedTags = [];	
							for (i = 0; i < vm.totalArtistTags.length; i++) {
								vm.selectedTags.push(vm.totalArtistTags[i].id);
							}

							if(vm.firstArtistTags != 0) {
								vm.showPreviousButtonArtistTag = true;
							}
							else {
								vm.showPreviousButtonArtistTag = false;
							}

							if(vm.artistTags.length > 3) {
								vm.showNextButtonArtistTag = true;
								vm.artistTags.splice(vm.artistTags.length -1, 1);
							}
							else{
								vm.showNextButtonArtistTag = false;
							}							
						});
					});
				};

				vm.getNextArtistTag = function() {								
					vm.firstArtistTags += 3;
					vm.reloadArtistTags();					
				}

				vm.getPreviousArtistTag = function() {
					vm.firstArtistTags -= 3;
					if(vm.firstArtistTags < 0){
						vm.firstArtistTags = 0;
					}
					vm.reloadArtistTags();
				}

				function changeSelectedTag (id)  {
					vm.selectedTag = id; //TODO: esto se usa?
				}

				vm.addSelectedTags = function () {
					//recorremos tags guardados
					//por cada tag guardado, comprobamos que existe en los seleccionados.
					//en caso de no existir en los seleccionados, hay que borrar el tag guardado.
					var selected = false;
					for (i = 0; i < vm.totalArtistTags.length; i++) { 
						selected = false;
						for (j = 0; j < vm.selectedTags.length; j++) {
							if (vm.totalArtistTags[i].id == vm.selectedTags[j]){
								selected = true;
							}
						}
						if (!selected) {
							vm.deleteSelectedTag(vm.totalArtistTags[i].id);
						}				
					}					

					var exists = false;
					for (i = 0; i < vm.selectedTags.length; i++) { 
						exists = false;
						for (j = 0; j < vm.totalArtistTags.length; j++) {
							if (vm.totalArtistTags[j].id == vm.selectedTags[i]){
								exists = true;
							}
						}
						if (!exists) {
							vm.addSelectedTag(vm.selectedTags[i]);
						}				
					}
				}

                vm.addSelectedTag = function (selected) {
                   	vm.addTagToArtist.$save({ tagId: selected, artistId: vm.artistId})
						.then(function (result) {
							vm.reloadArtistTags();							
							$mdToast.show(
								$mdToast.simple()
									.textContent('Tag añadido correctamente!')
									.position('top right')
									.hideDelay(3000)
								);
							//$state.reload();	
						}, function (error) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('Error añadiendo el tag.')
									.position('top right')
									.hideDelay(3000)
								);
							console.error(error);
							//vm.nuevoMovimiento.importe = -9999;
						});				
				};

				vm.deleteSelectedTag = function (tagId) {					
                   	vm.addTagToArtist.$delete({ tagId: tagId, artistId: vm.artistId })
						.then(function (result) {
							vm.reloadArtistTags();
							$mdToast.show(
								$mdToast.simple()
									.textContent('Tag eliminado correctamente!')
									.position('top right')
									.hideDelay(3000)
								);
							//$state.reload();	

							// cuando ha terminado el guardado del movimiento
							// es momento de pedir una actualización de datos
							//vm.nuevoMovimiento.importe = 0;
						}, function (error) {
							alert(error);
							$mdToast.show(
								$mdToast.simple()
									.textContent('Error eliminando el tag.')
									.position('top right')
									.hideDelay(3000)
								);
							console.error(error);
							//vm.nuevoMovimiento.importe = -9999;
						});				
				};

				vm.hideDialog = $mdDialog.hide;
					//vm.showDialog = vm.showDialog;

				vm.showDialog = function(evt) {
					vm.dialogOpen = true;
					$mdDialog.show({
					targetEvent: evt,
					controller: function () { 
						this.parent = vm; 
						},
					controllerAs: 'ctrl',
					templateUrl: './states/artist/editTagsDialog.html'
						// '<md-dialog>' +
						// '  <md-content>{{ctrl.parent.greeting}}, world !</md-content>' +
						// '  <div class="md-actions">' +
						// '    <md-button ng-click="ctrl.parent.dialogOpen=false;ctrl.parent.hideDialog()">' +
						// '      Close' +
						// '    </md-button>' +
						// '  </div>' +
						// '</md-dialog>'
					});
				}

                vm.updateArtist = function () {
					//vm.nuevoMovimiento.fecha = new Date(vm.nuevoMovimiento.fecha);  
					vm.sendArtist.id = vm.editArtist.id;
					vm.sendArtist.name = vm.editArtist.name;
					vm.sendArtist.description = vm.editArtist.description;
					vm.sendArtist.image = vm.editArtist.image;


                    vm.sendArtist.$update({ id: artistId })
						.then(function (result) {
							$mdToast.show(
								$mdToast.simple()
									.textContent('¡Artista actualizado correctamente!')
									.position('top right')
									.hideDelay(3000)
								);
							$state.go('getAllArtists');
						}, function (error) {
							console.error(error);
								$mdToast.show(
									$mdToast.simple()
										.textContent('Error: el artista no ha podido actualizarse.')
										.position('top right')
										.hideDelay(4000)
									);
						});
				};


				vm.reloadArtistTags();
				vm.loadTags();


			}
		})

}());