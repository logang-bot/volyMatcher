package com.restrusher.volymatcher.ui.screens.scan

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.restrusher.volymatcher.R
import com.restrusher.volymatcher.data.source.SampleDataSource
import com.restrusher.volymatcher.ui.theme.VolyMatcherTheme

@Composable
fun BodyScanScreen(
    playerId: String?,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    viewModel: BodyScanViewModel = viewModel(factory = BodyScanViewModel.factory(playerId)),
) {
    val context = LocalContext.current
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasCameraPermission = granted }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    BodyScanContent(
        uiState = uiState,
        hasCameraPermission = hasCameraPermission,
        onRequestPermission = { permissionLauncher.launch(Manifest.permission.CAMERA) },
        modifier = modifier,
        onBack = onBack,
    )
}

@Composable
private fun BodyScanContent(
    uiState: BodyScanUiState,
    hasCameraPermission: Boolean,
    onRequestPermission: () -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
) {
    val player = uiState.player
    val accentColor = MaterialTheme.colorScheme.primary
    val heroBg = MaterialTheme.colorScheme.inverseSurface
    val heroFg = MaterialTheme.colorScheme.inverseOnSurface

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(heroBg),
        contentPadding = PaddingValues(bottom = 40.dp),
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(heroFg.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center,
                ) {
                    IconButton(onClick = onBack, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Rounded.ArrowBack, "Back", tint = heroFg, modifier = Modifier.size(16.dp))
                    }
                }
                Text(text = stringResource(R.string.scan_step), style = MaterialTheme.typography.labelMedium, color = heroFg.copy(alpha = 0.6f))
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(heroFg.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("?", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = heroFg)
                }
            }
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = if (player != null) stringResource(R.string.scan_instruction, player.name)
                           else stringResource(R.string.scan_instruction_new),
                    style = MaterialTheme.typography.headlineLarge,
                    color = heroFg,
                )
                Text(text = stringResource(R.string.scan_position), style = MaterialTheme.typography.labelMedium, color = heroFg.copy(alpha = 0.6f))
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            val viewportModifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .aspectRatio(3f / 4f)

            if (hasCameraPermission) {
                CameraViewport(
                    hasCameraPermission = true,
                    accentColor = accentColor,
                    heroFg = heroFg,
                    player = player,
                    modifier = viewportModifier,
                )
            } else {
                CameraPermissionRequest(
                    onRequestPermission = onRequestPermission,
                    modifier = viewportModifier,
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(stringResource(R.string.scan_progress_label), style = MaterialTheme.typography.labelMedium, color = heroFg.copy(alpha = 0.6f))
                    Text("82%", style = MaterialTheme.typography.labelMedium, color = accentColor)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(heroFg.copy(alpha = 0.1f)),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.82f)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(accentColor),
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            ScanStatsGrid(player = player, accentColor = accentColor, heroFg = heroFg)
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(accentColor)
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.scan_hold_steady),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.White,
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Body Scan — Light")
@Composable
private fun BodyScanLightPreview() {
    VolyMatcherTheme(darkTheme = false) {
        BodyScanContent(
            uiState = BodyScanUiState(player = SampleDataSource.players.first()),
            hasCameraPermission = false,
            onRequestPermission = {},
        )
    }
}
