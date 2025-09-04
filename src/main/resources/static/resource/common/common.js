function successAlert(msg) {
  Swal.fire({
    text: msg,
    icon: 'success',
    confirmButtonText: '확인'
  })
}

function confirmModal(msg, confirmCallback) {
  Swal.fire({
    text: msg,
    icon: "warning",
    showCancelButton: true,
    confirmButtonText: "삭제",
    cancelButtonText: "취소",
    confirmButtonColor: "#d33",
    cancelButtonColor: "#3085d6"
  }).then((result) => {
    if (result.isConfirmed && confirmCallback) {
      confirmCallback();
    }
  });
}