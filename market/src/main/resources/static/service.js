const form = document.querySelector('#form');
const input = document.querySelector('#input');

const uploadFile = (event) => {
	event.preventDefault();
	const blobFile = document.querySelector('#input').files[0];
	console.log(blobFile);
	const formData = new FormData();
	formData.append('file', blobFile);
	const res = fetch('http://localhost:8082/image', {
		method: "POST",
		body: formData
	});
}

form.addEventListener('submit', uploadFile)