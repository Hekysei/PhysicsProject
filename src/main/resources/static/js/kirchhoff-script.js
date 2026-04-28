function applyCircuitColors(results) {
    if (!results) return;

    var iTot = Math.abs(results.totalCurrent);
    var iR2 = Math.abs(results.currentR2);
    var iR3 = Math.abs(results.currentR3);
    var iR5 = Math.abs(results.currentR5);
    var iR6 = Math.abs(results.currentR6);

    var maxI = Math.max(iTot, iR2, iR3, iR5, iR6);
    var minI = Math.min(iTot, iR2, iR3, iR5, iR6);

    var legendMin = document.getElementById('legend-min');
    var legendMax = document.getElementById('legend-max');
    if(legendMin) legendMin.textContent = minI.toFixed(1) + ' mA';
    if(legendMax) legendMax.textContent = maxI.toFixed(1) + ' mA';

    function getColor(current) {
        if (maxI === minI) return "hsl(240, 100%, 55%)";
        var ratio = (current - minI) / (maxI - minI);
        var hue = 240 - (ratio * 240);
        return "hsl(" + hue + ", 100%, 55%)";
    }

    function applyColor(className, current) {
        var color = getColor(current);
        var elements = document.querySelectorAll('.' + className);
        elements.forEach(function(el) {
            if (el.tagName.toLowerCase() === 'polygon') {
                el.style.fill = color;
            } else {
                el.style.stroke = color;
            }
        });
    }

    applyColor('wire-tot', iTot);
    applyColor('wire-r2', iR2);
    applyColor('wire-r3', iR3);
    applyColor('wire-r5', iR5);
    applyColor('wire-r6', iR6);
}

function initSidebarResize() {
    var container = document.querySelector('.container');
    var sidebar = document.querySelector('.sidebar');
    var resizer = document.getElementById('sidebar-resizer');
    if (!container || !sidebar || !resizer) return;

    var root = document.documentElement;
    var storageKey = 'kirchhoff-sidebar-width-v2';
    var mediaQuery = window.matchMedia('(max-width: 1220px)');
    var minWidth = 360;

    function getMaxWidth() {
        return Math.max(minWidth, Math.floor(window.innerWidth / 2));
    }

    function clampWidth(width) {
        var maxWidth = getMaxWidth();
        return Math.min(Math.max(width, minWidth), maxWidth);
    }

    function applyWidth(width) {
        var nextWidth = clampWidth(width);
        root.style.setProperty('--sidebar-width', nextWidth + 'px');
        try {
            window.localStorage.setItem(storageKey, String(nextWidth));
        } catch (error) {
            /* Ignore storage errors. */
        }
    }

    if (!mediaQuery.matches) {
        try {
            var savedWidth = parseInt(window.localStorage.getItem(storageKey), 10);
            if (!Number.isNaN(savedWidth)) {
                applyWidth(savedWidth);
            }
        } catch (error) {
            /* Ignore storage errors. */
        }
    }

    function handlePointerMove(event) {
        if (mediaQuery.matches) return;
        var containerRect = container.getBoundingClientRect();
        var nextWidth = event.clientX - containerRect.left;
        applyWidth(nextWidth);
    }

    function stopResize() {
        document.body.classList.remove('layout-resizing');
        window.removeEventListener('pointermove', handlePointerMove);
        window.removeEventListener('pointerup', stopResize);
    }

    resizer.addEventListener('pointerdown', function(event) {
        if (mediaQuery.matches) return;
        event.preventDefault();
        document.body.classList.add('layout-resizing');
        resizer.setPointerCapture(event.pointerId);
        window.addEventListener('pointermove', handlePointerMove);
        window.addEventListener('pointerup', stopResize);
    });

    mediaQuery.addEventListener('change', function(e) {
        if (e.matches) {
            stopResize();
            root.style.removeProperty('--sidebar-width');
        } else {
            var currentWidth = sidebar.getBoundingClientRect().width;
            applyWidth(currentWidth);
        }
    });

    window.addEventListener('resize', function() {
        if (!mediaQuery.matches) {
            applyWidth(sidebar.getBoundingClientRect().width);
        }
    });
}