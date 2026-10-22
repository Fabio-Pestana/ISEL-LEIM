/**
 * 
 */
 function shadeColor(color, percent) {
    let R = parseInt(color.substring(1, 3), 16);
    let G = parseInt(color.substring(3, 5), 16);
    let B = parseInt(color.substring(5, 7), 16);

    R = parseInt(R * (100 + percent) / 100);
    G = parseInt(G * (100 + percent) / 100);
    B = parseInt(B * (100 + percent) / 100);

    R = (R < 255) ? R : 255;  
    G = (G < 255) ? G : 255;  
    B = (B < 255) ? B : 255;  

    R = (R < 0) ? 0 : R;
    G = (G < 0) ? 0 : G;
    B = (B < 0) ? 0 : B;

    let RR = ((R.toString(16).length == 1) ? "0" + R.toString(16) : R.toString(16));
    let GG = ((G.toString(16).length == 1) ? "0" + G.toString(16) : G.toString(16));
	let BB = ((B.toString(16).length == 1) ? "0" + B.toString(16) : B.toString(16));

	return "#" + RR + GG + BB;
}
        
function applyButtonColor(button, color) {
	let originalColor = color;
    let darkerColor = shadeColor(originalColor, -20);

    button.addEventListener('mouseover', function() {
    		button.style.backgroundColor = darkerColor;
    });

    button.addEventListener('mouseout', function() {
          button.style.backgroundColor = originalColor;
    });
}