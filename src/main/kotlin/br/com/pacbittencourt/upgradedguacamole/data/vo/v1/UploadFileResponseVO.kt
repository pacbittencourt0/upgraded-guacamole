package br.com.pacbittencourt.upgradedguacamole.data.vo.v1

data class UploadFileResponseVO(
    var fileName: String = "",
    var downloadUri: String = "",
    var fileType: String = "",
    var fileSize: Long = 0
)