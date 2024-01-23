package com.gyeolse.drinkingcalendar.ui.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.gyeolse.drinkingcalendar.R
import com.gyeolse.drinkingcalendar.ui.components.TodoItem
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
) {
    // Snackbar를 표시하기 위해서 ScaffoldState가 필요
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // 화면 전환시에도 살아있도록 하려면 rememberSaveable
    var text by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Test Calendar") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Row(Modifier.padding(8.dp)) {
                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        text = it
                    },
                    modifier = Modifier.weight(weight = 1f),    // 남은 영역 꽉 채우기
                    shape = RoundedCornerShape(8.dp),   // 둥글게 모양 내기
                    maxLines = 1,   // 한 줄 이상 늘어나지 않도록
                    placeholder = { Text("할 일") },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_next),
                            contentDescription = null,
                        )
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),  // 키보드 옵션
                    keyboardActions = KeyboardActions(onDone = {
                        viewModel.addTodo(text)
                        text = ""
                        focusManager.clearFocus()   // 키보드 숨기기
                    }),
                )
            }
            // 한 줄 라인
            Divider()

            LazyColumn(
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),   // 상하 좌우 패딩
                verticalArrangement = Arrangement.spacedBy(16.dp),  // 아이템간의 패딩
            ) {
                items(viewModel.items.value) { todoItem ->
                    Column {
                        TodoItem(
                            todo = todoItem,
                            onClick = { index ->
                                viewModel.toggle(index)
                            },
                            onDeleteClick = { index ->
                                viewModel.delete(index)
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "할 일 삭제됨",
                                        actionLabel = "취소",
                                    )

                                    if (result == SnackbarResult.ActionPerformed) {
                                        viewModel.restoreTodo()
                                    }
                                }
                            },
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = Color.Black, thickness = 1.dp)
                    }
                }
            }
            // 예전의 Recyclerview 라고 생각하면 된다
//            LazyColumn(
//                contentPadding = PaddingValues(16.dp),
//                verticalArrangement = Arrangement.spacedBy(16.dp),
//            ) {
//                items(viewModel.items.value) { todoItem ->
//                    Column {
//                        TodoItem(
//                            todo = todoItem,
//                            onClick = { todo -> viewModel.toggle(todo.uid) },
//                            onDeleteClick = { todo ->
//                                viewModel.delete(todo.uid)
//                                // scope 필요.
//                                scope.launch {
//                                    val result = snackbarHostState.showSnackbar(
//                                        message = "할 일 삭제됨",
//                                        actionLabel = "취소",
//                                    )
//                                    if (result == SnackbarResult.ActionPerformed) {
//                                        viewModel.restoreTodo()
//                                    }
//                                }
//                            })
//                        Spacer(modifier = Modifier.height(16.dp))
//                        Divider(color = Color.Black, thickness = 1.dp)
//                    }
//                }
//            }
        }

    }
}