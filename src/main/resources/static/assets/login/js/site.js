jQuery(function($) {"use strict";
	var animEndEventNames = {
		'WebkitAnimation' : 'webkitAnimationEnd',
		'OAnimation' : 'oAnimationEnd',
		'msAnimation' : 'MSAnimationEnd',
		'animation' : 'animationend'
	}, animEndEventName = animEndEventNames[ Modernizr.prefixed('animation')], isMobile = navigator.userAgent.match(/(iPhone|iPod|Android|BlackBerry|iPad|IEMobile|Opera Mini)/i), isIE9 = navigator.userAgent.match('MSIE 9.0'), support = Modernizr.cssanimations;

	var Site = {

		initialized : false,
		styles : [{
			"featureType" : "landscape",
			"stylers" : [{
				"saturation" : -100
			}, {
				"lightness" : 65
			}, {
				"visibility" : "on"
			}]
		}, {
			"featureType" : "poi",
			"stylers" : [{
				"saturation" : -100
			}, {
				"lightness" : 51
			}, {
				"visibility" : "simplified"
			}]
		}, {
			"featureType" : "road.highway",
			"stylers" : [{
				"saturation" : -100
			}, {
				"visibility" : "simplified"
			}]
		}, {
			"featureType" : "road.arterial",
			"stylers" : [{
				"saturation" : -100
			}, {
				"lightness" : 30
			}, {
				"visibility" : "on"
			}]
		}, {
			"featureType" : "road.local",
			"stylers" : [{
				"saturation" : -100
			}, {
				"lightness" : 40
			}, {
				"visibility" : "on"
			}]
		}, {
			"featureType" : "transit",
			"stylers" : [{
				"saturation" : -100
			}, {
				"visibility" : "simplified"
			}]
		}, {
			"featureType" : "administrative.province",
			"stylers" : [{
				"visibility" : "off"
			}]
		}, {
			"featureType" : "water",
			"elementType" : "labels",
			"stylers" : [{
				"visibility" : "on"
			}, {
				"lightness" : -25
			}, {
				"saturation" : -100
			}]
		}, {
			"featureType" : "water",
			"elementType" : "geometry",
			"stylers" : [{
				"hue" : "#ffff00"
			}, {
				"lightness" : -25
			}, {
				"saturation" : -97
			}]
		}],
		initialize : function() {

			if (this.initialized)
				return;
			this.initialized = true;

			this.build();
			this.events();
			this.validation();
			this.inputAnimation();
			if ($('#header').length) {
				this.headerStyle();
			}
			if ($('.gallery-grid').length) {
				this.masonry();
			}

		},

		build : function() {

			//equalizer
			if ($('.equalizer').length) {

				var $equalizer = $('.equalizer'), equalizing, equalizer = new Array()

				equalizing = function($this, eqHeight) {
					if ($(window).width() >= 768) {
						$this.children().height(eqHeight);
					} else {
						$this.children().css('height', 'auto');
					}
				}
				var insideEqui = function() {
					for (var i = 0; i < $equalizer.length; i++) {
						equalizer[i] = $equalizer.eq(i)
						var eqHeight = equalizer[i].height();
						equalizing(equalizer[i], eqHeight);
					}
				}
				insideEqui();

				$(window).resize(function() {
					for (var i = 0; i < $equalizer.length; i++) {
						equalizer[i] = $equalizer.eq(i)
						equalizer[i].children().css('height', 'auto');
						insideEqui();
					}

				})
			}
			//macbook slider
			if ($('#mackbook-slider').length) {
				$('#mackbook-slider').flexslider({
					animation : "slide",
					controlNav : true,
					directionNav : true,
					init : function() {
						var $iconList = $('.icon-box >li');
						$iconList.find('a').on('click touchend', function(e) {
							e.preventDefault();
							$iconList.find('a').removeClass('active');
							$(this).addClass('active');
							var ind = $(this).parent('li').index();
							$('#mackbook-slider .flex-control-paging').find('li').eq(ind).find('a').trigger('click');
						})
						$('.preview-block .badge-theme').click(function(e) {
							e.preventDefault();
							$('.preview-block .badge-theme').css('z-index', '10');
							if ($(this).hasClass('next-slide')) {
								$('#mackbook-slider .flex-next ').trigger('click');
								$(this).css('z-index', '100');
							} else if ($(this).hasClass('prev-slide')) {
								$('#mackbook-slider .flex-prev ').trigger('click');
								$(this).css('z-index', '100');
							}
						})
						setInterval(function() {
							var ind = $('#mackbook-slider .flex-control-paging .flex-active').parent('li').index();
							$iconList.find('a').removeClass('active');
							$iconList.eq(ind).find('a').addClass('active');
						}, 10)
					}
				});
			}
			//triangular boxes
			var triLayout = function() {
				$('.l-down, .l-down-up').css({
					'border-left-width' : parseInt($('#wrapper').width())
				})
				if ($('.device-frame, .flip-frame, .box-frame').length) {
					$('.device-frame, .flip-frame, .box-frame').addClass('animate');
					$('.ipad-loader').fadeOut(600);
				}
				if ($('.iphone-wrap').length) {
					$('.iphone-wrap .iphone-frame:first-child').addClass('animate');
					setTimeout(function() {
						$('.iphone-wrap .iphone-frame.large').addClass('animate');
					}, 200)
					$('.ipad-loader').fadeOut(600);
				}
			}
			if ($('.l-down').length) {
				$(window).load(function() {
					triLayout()
				})
				$(window).resize(function() {
					triLayout()
				})
				$(document).on('click', '#layout .layout-column', function() {
					setInterval(function() {
						triLayout()
					}, 400)
				})
			}

			if ($('.intro-section figure .mobile-preview-first').length) {
				$('.intro-section figure .mobile-preview-first').addClass('animate');
				setTimeout(function() {
					$('.intro-section figure .mobile-preview-sec').addClass('animate');
				}, 200)
				$('.ipad-loader').fadeOut(600);
			}
			if ($('.iphone-frame-left').length) {
				$('.iphone-frame-right').addClass('animate');
				setTimeout(function() {
					$('.iphone-frame-left').addClass('animate');
				}, 200)
				$('.ipad-loader').fadeOut(600);
			}
			if ($('.ipad-frame-wrap').length) {
				setTimeout(function() {
					$('.ipad-frame-large').addClass('animate');
					setTimeout(function() {
						$('.ipad-frame-wrap .iphone-frame').addClass('animate');
					}, 500)
					$('.ipad-loader').fadeOut(600);
				}, 1000)
			}

			//Site Logo GreyScale
			if ($('#partner-slide').length) {
				var $partnerSlide = $("#partner-slide")
				$partnerSlide.owlCarousel({
					autoPlay : 3000,
					items : 3,
					itemsCustom : [[0, 1], [450, 2], [600, 3]],
					pagination : false,
					slideSpeed : 400,
					addClassActive : true,
					beforeInit : function() {
						$partnerSlide.fadeIn(300)
					}
				});
			}
			if ($('#partner-slide-full').length) {
				var $partnerSlideFull = $("#partner-slide-full")
				$partnerSlideFull.owlCarousel({
					autoPlay : 3000,
					items : 6,
					itemsCustom : [[0, 1], [450, 2], [600, 3], [992, 4], [1200, 6]],
					pagination : false,
					slideSpeed : 400,
					addClassActive : true,
					beforeInit : function() {
						$partnerSlideFull.fadeIn(300)
						$partnerSlideFull.after('<span class="slide-divider"/><span class="slide-divider"/><span class="slide-divider"/>')
					}
				});
			}

			//circular progressbar
			if ($(".circle-progress").length) {
				var $circleProgress = $(".circle-progress");
				$circleProgress.circliful();
				$(document).on('click', '.swatches a', function() {
					var color = $(this).attr('data-color-hex')
					$.cookie("skin", color);
					$circleProgress.html(' ')
					$circleProgress.circliful({
						foregroundColor : color
					});
				})
			}

			//pricing block
			if ($('.pricing-block').length) {
				var $pricingBlock = $('.pricing-block'), $pricingBlockList = $('.pricing-block .list-group'), pricingBlockListLength = $pricingBlock.length, $pricingItems = $('.pricing-block .list-group-item');
				var array = new Array();
				$pricingItems.hover(function(e) {
					e.stopPropagation()
					var ind = $(this).index();
					$pricingItems.removeClass('active')
					for (var i = 0; i < pricingBlockListLength; i++) {
						$pricingBlock.eq(i).find('.list-group-item').eq(ind).addClass('active')
					}
				})
				$pricingBlock.hover(function() {
					$pricingItems.removeClass('active')
				})
			}

			//countdown

			if ($('#countdown').length) {
				var austDay = new Date();
				austDay = new Date(2015, 6, 2);
				$('#countdown').countdown({
					until : austDay
				});
			}

			// content slider
			if ($('#slide-sec-top').length) {
				$('#slide-sec-top .flexslider').flexslider({
					controlNav : false,
					animation : "slide",
					start : function() {
						$('#slide-sec-top').find('.flex-prev').addClass('badge badge-theme').html('<i class="fa fa-chevron-left"></i>')
						$('#slide-sec-top').find('.flex-next').addClass('badge badge-theme').html('<i class="fa fa-chevron-right"></i>')
					}
				});
			}
			// social icon
			if ($('.sec-social').length) {

				if (isMobile) {
					$('.sec-social').on('touchend', function(e) {
						e.stopPropagation()
						$(this).find('.social-icons').addClass('animate')
					});
					$(document).on('touchend', function() {
						$('.sec-social').find('.social-icons').removeClass('animate')
					});
				} else {
					$('.sec-social').hover(function() {
						$(this).find('.social-icons').addClass('animate')
					}, function() {
						$(this).find('.social-icons').removeClass('animate')
					});
				}
			}
			//fancybox
			if ($('.fancybox').length) {
				var lighboxSlider = true;
				$('.fancybox').fancybox({
					padding : 0,
					autoHeight : true,
					fitToView : false,
					scrolling : true,
					helpers : {
						overlay : true,
						title : false
					},
					afterShow : function() {
						//lightbox flexslider
						if ($('.lightbox-gallery .flexslider').length && lighboxSlider == true) {
							$('.lightbox-gallery .flexslider').flexslider({
								animation : "slide",
								start : function() {
									$('.lightbox-gallery .flexslider').find('.flex-prev').addClass('badge badge-theme').html('<i class="fa fa-chevron-left"></i>')
									$('.lightbox-gallery .flexslider').find('.flex-next').addClass('badge badge-theme').html('<i class="fa fa-chevron-right"></i>')
								}
							});
							lighboxSlider = false
						}
					}
				});
			}

		},
		masonry : function() {
			var $container = $('.gallery-grid');

			$container.imagesLoaded(function() {
				$container.masonry({
					itemSelector : '.item',
					columnWidth : '.grid-sizer',
					gutter : '.gutter-sizer'
				});
			});

			$('.filters a').click(function(e) {
				var menuactive = $(this).attr('data-filter');
				var category = $(this).attr('data-filter').replace('#', '');
				$('.filters').find('a').removeClass('selected');
				$(this).addClass('selected');

				$container.find('.item').removeClass('hide');
				$container.find('.item:not(.' + category + ')').addClass('hide');
				$container.masonry({
					itemSelector : '.item',
					columnWidth : '.grid-sizer',
					gutter : '.gutter-sizer'
				});

				$container.find('.' + category).show(500);
				$container.find('.item:not(.' + category + ')').hide(500);
				e.preventDefault();

			});

		},

		events : function() {

			//animated section
			if ($('.animated-section').length) {
				var $animSections = $('.animated-section');
				var animatedSection = new Array()
				for (var i = 0; i < $animSections.length; i++) {
					animatedSection[i] = $animSections.eq(i)
				}
			}
			var animSection = function() {
				for (var i = 0; i < $animSections.length; i++) {
					if ($(window).scrollTop() > animatedSection[i].offset().top - $(window).height() * .7) {
						animatedSection[i].addClass('animate')
					}
				}

			}
			//bg parallax
			if ($('.parallax').length) {
				var $parallax = $('.parallax');
				var parallaxArray = new Array();
				for (var i = 0; i < $parallax.length; i++) {
					parallaxArray[i] = $parallax.eq(i)
				}

			}

			var parallax = function(id, val) {
				if ($(window).scrollTop() > id.offset().top - $(window).height() && $(window).scrollTop() < id.offset().top + id.outerHeight()) {
					var px = parseInt($(window).scrollTop() - (id.offset().top - $(window).height()))
					px *= -val;
					id.css({
						'background-position' : 'center ' + px + 'px'
					})
				}
			}
			// navigation link hightlight on scroll

			var navHighlight = function() {
				var i = 0, j = 0, ids = [], offsetId = [], hHeight = $('#header.horz').outerHeight(), scrollTop = $(window).scrollTop(), $navbarLinks = $('.navbar-nav >li'), navLength = $navbarLinks.length;
				$navbarLinks.each(function() {
					if (!$(this).find('a').attr('data-link')) {
						ids[i] = $(this).find('a').attr('href');
						offsetId[i] = $(ids[i]).offset().top - hHeight
					} else {
						ids[i] = $(this).find('a').attr('data-link');
						offsetId[i] = $(ids[i]).offset().top - hHeight
					}

					i++;
				})
				//nav active
				$navbarLinks.removeClass('active');
				for ( j = 0; j < navLength; j++) {
					if (j < navLength - 1) {
						if (scrollTop >= offsetId[j] && scrollTop < offsetId[j + 1]) {
							$('.navbar-nav >li> a[data-link="' + ids[j] + '"]').parent('li').addClass('active');
							$('.navbar-nav >li> a[href="' + ids[j] + '"]').parent('li').addClass('active');
						}
					} else {
						if (scrollTop >= offsetId[navLength - 1]) {
							$('.navbar-nav >li> a[data-link="' + ids[navLength - 1] + '"]').parent('li').addClass('active');
							$('.navbar-nav >li> a[href="' + ids[navLength - 1] + '"]').parent('li').addClass('active');
						}
					}
				}
			}
			if ($(window).width() >= 992 && $('#header').length && $('#header').hasClass('horz')) {
				if ($('#header.horz').length) {
					navHighlight();
				}
				$(window).scroll(function() {
					if ($('#header.horz').length) {
						navHighlight();
					}
				})
			}

			//goto to sections on navigation link
			var scrollTo = function(id) {
				var top = $(id).offset().top
				$('html, body').animate({
					scrollTop : parseInt(top - 100)
				}, 700)
			}
			// goto next section
			if ($('#homepage').length) {
				if ($('#header').hasClass('horz')) {
					$('#header .navbar-nav a').click(function(e) {
						e.preventDefault()
						var id = $(this).attr('href');
						scrollTo(id);
					})
				} else {
					$('#header .navbar-nav a').click(function(e) {
						e.preventDefault()
						var id = $(this).attr('href');
						scrollTo(id);
						$('#header .navbar-toggle').trigger('click')
					})
				}
			}

			//blog-section
			var fixedBlog = function() {
				var $mainBlog = $('#main-blog'), $headerNav = $('#header .navbar-header'), $blogSec = $('.blog-section');
				if ($(window).scrollTop() >= $mainBlog.offset().top - $headerNav.outerHeight() && $(window).scrollTop() < $('.wrap-blog-post').offset().top) {
					$blogSec.addClass('fixed').css({
						top : $headerNav.outerHeight()
					})
					$mainBlog.css({
						paddingTop : $blogSec.outerHeight()
					})
				} else {
					$blogSec.removeClass('fixed').css({
						top : 0
					})
					$mainBlog.css({
						paddingTop : 0
					})
				}
			}
			//iconAnimate
			var animOnce = true, positions = [{
				'left' : '10%',
				'top' : '5%'
			}, {
				'left' : '3%',
				'top' : '31%'
			}, {
				'left' : '10.2%',
				'top' : '56%'
			}, {
				'right' : '9%',
				'top' : '4%'
			}, {
				'right' : '5%',
				'top' : '31%'
			}, {
				'right' : '10%',
				'top' : '56%'
			}]
			var iconAnimate = function() {

				var $iconList = $('.icon-box >li'), i = 0;
				var icons = function(val) {
					if (i < 4 && i < $iconList.length) {
						$iconList.eq(val).addClass('animate').animate({
							left : positions[val].left,
							top : positions[val].top,
							opacity : 1
						}, 400, 'easeOutBack', function() {
							icons(i++)
						})
					} else if (i >= 4 && i <= $iconList.length) {
						$iconList.eq(val).addClass('animate').animate({
							right : positions[val].right,
							top : positions[val].top,
							opacity : 1
						}, 400, 'easeOutBack', function() {
							icons(i++)
						})
					}
				}
				icons(i)
				animOnce = false

			}
			//condition for desktop only
			if (!isMobile) {

				if ($('.animated-section').length) {
					$animSections.removeClass('animate')
					animSection();
				}

				$(window).load(function() {

					if ($('.parallax').length) {
						for (var i = 0; i < $parallax.length; i++) {
							parallax(parallaxArray[i], 0.3);
						}

					}
				})
				if ($('.icon-box').length) {
					if ($(window).scrollTop() >= $('.preview-block').offset().top - ($(window).height() * 0.1) && animOnce == true) {
						iconAnimate();
					}
				}
				if ($('#main-blog').length) {
					fixedBlog()
				}

				$(window).scroll(function() {
					if ($('.animated-section').length) {
						animSection();
					}
					if ($('.parallax').length) {
						for (var i = 0; i < $parallax.length; i++) {
							parallax(parallaxArray[i], 0.3);
						}
					}
					if ($('.icon-box').length) {
						if ($(window).scrollTop() >= $('.preview-block').offset().top - ($(window).height() * 0.1) && animOnce == true) {
							iconAnimate();
						}
					}
					if ($('#main-blog').length) {
						fixedBlog()
					}
				})
			} else if (isMobile && $(window).width() >= 1200) {

				if ($('.icon-box').length) {
					iconAnimate();
				}
			}

			// ipad-frame video
			if ($('.ipad-frame').length) {
				var autoPlayVideo = function(vcode) {
					$('.ipad-frame .embed-responsive').html('<iframe src="https://www.youtube.com/embed/' + vcode + '?autoplay=1&loop=1&rel=0&wmode=transparent" frameborder="0" allowfullscreen wmode="Opaque"></iframe>');
				}
				if (!isMobile) {
					$('.ipad-frame .embed-responsive').on('click', function() {
						autoPlayVideo('wN3gueLT0D8');
					})
				} else {
					autoPlayVideo('wN3gueLT0D8');
				}
			}

			// ipad-frame video

			if ($('.play-btn').length) {
				$('.play-btn').fancybox({
					openEffect : 'none',
					closeEffect : 'none',
					prevEffect : 'none',
					nextEffect : 'none',
					padding : 0,
					closeBtn : false,
					helpers : {
						media : {},
						buttons : {},
						title : {
							type : 'inside'
						},
						overlay : {
							css : {
								'background' : 'rgba(0,0,0,0.85)'
							}
						}
					}
				});
			}

			//Custom Map
			if ($('#map').length) {
				var map = new GMaps({
					div : '#map',
					lat : 41.402619,
					lng : -74.333062,
					disableDefaultUI : true,
					zoom : 17,
					scrollwheel : false
				});
				map.drawOverlay({
					lat : map.getCenter().lat(),
					lng : map.getCenter().lng(),
					content : '<a href="#"><img src="assets/img/map-marker.png" alt=""></a>',
					verticalAlign : 'top',
					horizontalAlign : 'center'
				});

				if ($(window).width() >= 1200) {
					map.setOptions({
						styles : Site.styles,
						center : new google.maps.LatLng(41.401836, -74.329801),
					});
				} else if ($(window).width() >= 992) {
					map.setOptions({
						styles : Site.styles,
						center : new google.maps.LatLng(41.401836, -74.331801),
					});
				} else if ($(window).width() >= 768) {
					map.setOptions({
						styles : Site.styles,
						center : new google.maps.LatLng(41.401836, -74.329801),
					});
				} else {
					map.setOptions({
						styles : Site.styles,
						center : new google.maps.LatLng(41.400136, -74.332562),
					});
				}
			}

			//image in lightbox
			if ($('.zoom-view').length) {
				$('.zoom-view').fancybox({
					padding : 0,
					helpers : {
						media : {},
						buttons : {},
						title : {
							type : 'inside'
						},
						overlay : {
							css : {
								'background' : 'rgba(0,0,0,0.85)'
							}
						}
					}
				});
			}

			/*work-process section animation code starts here*/
			if (!isMobile && !isIE9 && $('#work-process').length) {

				$('.left-rotate').rotate('-50deg');
				$('.right-rotate').rotate('130deg');

				$('.step-block.left, .step-block.right, .step-block.left .table-cell').css({
					'opacity' : 0
				});
				$('.left-rotate, .right-rotate, .step-directions .badge-theme').css({
					'opacity' : 0,
					'visibility' : 'hidden'
				});
				$('.step-directions .badge-theme .fa').css('display', 'none');
				$('.step-block.left').css('border', 'none');
			}
			//flip slider
			var flipSlides, slideCount = true, flipSlider = function() {
				var $flipSlider = $('#flip-slider'), $flipSlides = $flipSlider.find('.flip-slides'), $pages = $flipSlides.find('li'), flipLength = $flipSlides.find('li').length, currentInd = 0;
				$flipSlides.find('li').eq(0).addClass('current');
				flipSlides = function() {
					setInterval(function() {
						if (currentInd < flipLength - 1) {
							if (support) {
								$flipSlides.find('li').eq(currentInd + 1).addClass('flip-next')
								$flipSlides.find('.current').addClass('rotate').on(animEndEventName, function() {
									$flipSlides.find('li').removeClass('rotate flip-next current')
									$flipSlides.find('li').eq(currentInd).addClass('current');
								})
							} else {
								$flipSlides.find('li').hide().removeClass('current')
								$flipSlides.find('li').eq(currentInd).addClass('current').fadeIn(400);
							}
							currentInd = $flipSlides.find('li.current').index() + 1
						} else {
							if (support) {
								$flipSlides.find('li').eq(0).addClass('flip-next')
								$flipSlides.find('.current').addClass('rotate').on(animEndEventName, function() {
									$flipSlides.find('li').removeClass('rotate flip-next current')
									$flipSlides.find('li').eq(currentInd).addClass('current');

								})
							} else {
								$flipSlides.find('li').hide().removeClass('current')
								$flipSlides.find('li').eq(currentInd).addClass('current').fadeIn(400);
							}
							currentInd = 0
						}
					}, 5000)
				}
			}
			if ($('#flip-slider').length) {
				flipSlider();
			}
			//flip slider end
			var bool = true;
			var workProcessAnimation = function() {

				var b = 0;
				var secOffset = $('.design-section').offset().top;
				var secHt = $('.design-section').height();
				var a = 0;

				if ($(window).scrollTop() >= parseInt(secOffset + secHt / 2)) {

					setTimeout(function() {

						$('.step-block.left').animate({
							'opacity' : 1
						}, 1, function() {

							setTimeout(function() {
								$('.step-block.left').addClass('animate-left');
							}, 'fast');

							setTimeout(function() {

								$('.step-block.left').addClass('animate-right');
								setTimeout(function() {
									$('.step-block.left .table-cell').css({
										'opacity' : 1
									}).addClass('animated zoomIn');
									$('.step-block.left').addClass('b-radius');

								}, 1000);

							}, 900);

							setTimeout(function() {
								$('.borderleft').addClass('borderleftShow')
							}, 2800);

						});

					}, 100);

					setTimeout(function() {

						$('.step-directions .badge-theme').css({
							'visibility' : 'visible'
						}).animate({
							'opacity' : 1
						}, 300, function() {

							$('.step-directions .badge-theme .fa').fadeIn(400, function() {

								if (bool) {

									bool = false;
									var interval1 = setInterval(function() {

										b++;
										a = a + 100;

										if (b > 5) {
											clearInterval(interval1);
										}
										$('.step-directions .badge-theme .fa').animate({
											rotate : a + 'deg'
										}, 200);

									}, 300);
								}

							});

						});

						setTimeout(function() {
							$('.right-rotate').css({
								'visibility' : 'visible',
								'opacity' : 1
							})
							$('.right-rotate').animate({
								rotate : '180deg'
							}, 400);
						}, 800);

						setTimeout(function() {
							$('.left-rotate').css({
								'visibility' : 'visible',
								'opacity' : 1
							})
							$('.left-rotate').animate({
								rotate : '0deg'
							}, 300);
						}, 1100)

						setTimeout(function() {
							$('.step-directions .badge-theme').addClass('animate');
						}, 1800);

					}, 3300);

					setTimeout(function() {
						$('.right-sec').css({
							'opacity' : 1
						})
						if ($('#flip-slider').length && slideCount == true) {
							flipSlides();
							slideCount = false
						}

					}, 5900)
				}

			}
			if (!isMobile && !isIE9 && $('#work-process').length) {
				workProcessAnimation();
			} else if (isIE9 && $('#work-process').length) {
				$('.borderleft').addClass('borderleftShow')
				$('.step-directions .badge-theme').addClass('animate')
				if ($('#flip-slider').length) {
					flipSlides();
				}
			} else {
				if ($('#flip-slider').length) {
					flipSlides();
				}
			}
			$(window).scroll(function() {
				if (!isMobile && !isIE9 && $('#work-process').length) {
					workProcessAnimation();
				}
			});
			/*work-process section animation code ends here*/

		},

		inputAnimation : function() {
			// Input Animation
			$(".label-text").each(function() {
				var sop = '<span class="char">';
				var scl = '</span>';
				$(this).html(sop + $(this).html().split("").join(scl + sop) + scl);
				$(".ch:contains(' ')").html("&nbsp;")
			})
			var d;
			//animation time
			$(".input-group  input, .input-group  textarea").focus(function() {
				var tm = '-28px'
				$(this).next('.label-text').addClass("focussed").children().stop(true).each(function(i) {
					d = i * 50;
					$(this).delay(d).animate({
						top : tm
					}, 150, 'easeOutBack');
				})
			})
			$(".input-group  input, .input-group  textarea").blur(function() {

				//animate the label down if content of the input is empty
				if ($(this).val() == "") {
					$(this).next('.label-text').removeClass("focussed").children().stop(true).each(function(i) {
						d = i * 50;
						$(this).delay(d).animate({
							top : 0
						}, 150, 'easeInOutBack');
					})
				}
			})
		},
		validation : function() {
			var bool;

			$('#name,#email,#msg').val('').blur(function() {
				validateForm2(this);
			});

			$('#submit').click(function() {
				var i = 0;
				var x = $('#name').val();
				if (x == null || x == "" || x == "Name") {

					$('#name').closest('.input-group').addClass('error')
					bool = false;

				} else {
					i++;
					$('#name').closest('.input-group').removeClass('error');
					name_val = $('#name').val();

				}

				var x = $('#email').val();
				var atpos = x.indexOf("@");
				var dotpos = x.lastIndexOf(".");
				if (atpos < 1 || dotpos < atpos + 2 || dotpos + 2 >= x.length || x == 'Email') {
					$('#email').closest('.input-group').addClass('error')
					bool = false;
				} else {

					i++;
					$('#email').closest('.input-group').removeClass('error');
					email_val = $('#email').val();

				}
				var x = $('#msg').val();
				if (x == null || x == "" || x == "Message") {
					$('#msg').closest('.input-group').addClass('error')
					bool = false;
				} else {
					i++;
					$('#msg').closest('.input-group').removeClass('error');
					msg_val = $('#msg').val();

				}

				if (i == 3) {
					bool = true;
				}

				if (!bool) {
					return false;
				} else {
					$.post('mail.php', {
						name : name_val,
						email : email_val,
						msg : msg_val,
					}, function(data) {
						if (data == 1) {
							setTimeout(function() {
								$('#name').val('');
								$('#email').val('');
								$('#msg').val('');
								$('#name,#site,#msg').next().removeClass("focussed");
								$('.char').css('top', 0)

								alert('Thanks for using our template. Your message has been sent.');
							}, 500);
						}
					})
				}

			});

			function validateForm2(abc) {

				if ($(abc).val() != "") {
					$(abc).parent().removeClass('error');

				} else {
					$(abc).parent().addClass('error');

				}
				//email
				if ($(abc).attr('id') == 'email') {
					if (($(abc).val() != "" || $(abc).val() != null) && ($(abc).val().match(emailRegex))) {
						$(abc).parent().removeClass('error');

					} else {
						$(abc).parent().addClass('error');
					}
				}

			}

			var name_val = ''
			var email_val = '';

			var msg_val = '';
			var comp_val = '';
			var emailRegex = /^[a-zA-Z0-9._]+[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-zA-Z]{2,4}$/;
			var numericExpression = /^[(]{0,1}[0-9]{3}[)]{0,1}[-\s\.]{0,1}[0-9]{3}[-\s\.]{0,1}[0-9]{4}$/;
		},
		headerStyle : function() {
			// Fixed Header JS
			var initScroll = $(window).scrollTop(), headerHeight = $('#header').outerHeight(), sticky = $('#header').attr('data-sticky'), $HeaderNav = $('#header').find('.navbar')
			var fixedNav = function() {
				var currentScroll = $(window).scrollTop()
				function inteligent() {
					if (currentScroll >= initScroll) {
						$HeaderNav.removeClass('down')
						$HeaderNav.addClass('up')
						if (currentScroll == $(document).height() - $(window).height()) {
							$HeaderNav.removeClass('up')
							$HeaderNav.addClass('down')
						}
						initScroll = currentScroll
					} else {
						$HeaderNav.removeClass('up ')
						$HeaderNav.addClass('down')
						initScroll = currentScroll
					}
				}

				if (sticky == "yes") {
					if (currentScroll > $('#header').offset().top + $('#header').outerHeight()) {
						$HeaderNav.addClass('navbar-fixed-top')
						$('#header').css("padding-top", headerHeight)
						inteligent()
					} else {
						initScroll = currentScroll
						$HeaderNav.removeClass('navbar-fixed-top up down')
						$('#header').css("padding-top", "0")
					}
				} else {
					if (currentScroll > $('#header').offset().top + $('#header').outerHeight()) {
						$('#header').css("padding-top", headerHeight)
						$HeaderNav.addClass('navbar-fixed-top')
					} else {
						$HeaderNav.removeClass('navbar-fixed-top up down')
						$('#header').css("padding-top", "0")
					}
				}
			}
			fixedNav()
			$(window).scroll(function() {
				fixedNav()
			})
		}
	};

	Site.initialize();

})