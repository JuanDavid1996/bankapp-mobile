package com.example.bankapp.presentation.bank.helper

import android.Manifest
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission

class PermissionChecker {
    companion object {
        fun checkAppPermissions(event: PermissionListener) {
            TedPermission.create().setPermissionListener(event)
                .setDeniedMessage("Los permisos solicitado son para el correcto funcionamiento de la aplicación")
                .setPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                .check()
        }
    }
}