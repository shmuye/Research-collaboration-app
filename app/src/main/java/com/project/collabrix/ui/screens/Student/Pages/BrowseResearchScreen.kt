package com.project.collabrix.ui.screens.Student.Pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.project.collabrix.presentation.BrowseResearchViewModel
import com.project.collabrix.presentation.BrowseResearchUiState
import com.project.collabrix.presentation.ProjectStatus

@Composable
fun BrowseResearchScreen() {
    val viewModel: BrowseResearchViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F6FA))
            .padding(16.dp)
    ) {
        Text(
            text = "Browse Research",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        var search by remember { mutableStateOf("") }
        BasicTextField(
            value = search,
            onValueChange = {
                search = it
                viewModel.onSearchQueryChange(it)
            },
            singleLine = true,
            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
            decorationBox = { innerTextField ->
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    if (search.isEmpty()) {
                        Text("Search projects...", color = Color.Gray, fontSize = 16.sp)
                    }
                    innerTextField()
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        when (uiState) {
            is BrowseResearchUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.Black)
                }
            }
            is BrowseResearchUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text((uiState as BrowseResearchUiState.Error).message, color = Color.Red)
                }
            }
            is BrowseResearchUiState.Success -> {
                val data = uiState as BrowseResearchUiState.Success
                if (data.projects.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No projects found.", color = Color.Gray)
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        data.projects.forEach { project ->
                            ProjectCardBrowse(
                                project = project,
                                onApply = { viewModel.applyToProject(project.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProjectCardBrowse(
    project: com.project.collabrix.presentation.ProjectUiModel,
    onApply: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        border = ButtonDefaults.outlinedButtonBorder,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                project.title ?: "Untitled Project",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (!project.professorName.isNullOrBlank()) {
                Text(
                    "By ${project.professorName}",
                    color = Color(0xFF3B82F6),
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                project.description ?: "",
                color = Color.Gray,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Deadline: " + (project.deadline?.substring(0, 10) ?: "N/A"),
                    color = Color.Black,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(16.dp))
                StatusBadge(project.status)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onApply,
                enabled = project.status == ProjectStatus.OPEN && !project.isApplied,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (project.status == ProjectStatus.OPEN && !project.isApplied) Color(0xFF3B82F6) else Color.LightGray
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    if (project.isApplied) "Applied" else "Apply",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun StatusBadge(status: ProjectStatus) {
    val (text, color) = when (status) {
        ProjectStatus.OPEN -> "Open" to Color(0xFF22C55E)
        ProjectStatus.CLOSED -> "Closed" to Color(0xFFEF4444)
    }
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.15f),
        border = null
    ) {
        Text(
            text = text,
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
} 