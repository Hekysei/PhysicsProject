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