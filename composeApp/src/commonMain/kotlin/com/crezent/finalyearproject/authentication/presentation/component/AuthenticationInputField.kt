package com.crezent.finalyearproject.authentication.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.crezent.finalyearproject.ui.theme.NunitoFontFamily
import com.crezent.finalyearproject.ui.theme.black
import com.crezent.finalyearproject.ui.theme.errorColor
import com.crezent.finalyearproject.ui.theme.grey
import com.crezent.finalyearproject.ui.theme.largePadding
import com.crezent.finalyearproject.ui.theme.mediumPadding
import com.crezent.finalyearproject.ui.theme.smallPadding
import finalyearproject.composeapp.generated.resources.Res
import finalyearproject.composeapp.generated.resources.eye
import finalyearproject.composeapp.generated.resources.open_eye
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun AuthenticationInputField(
    modifier: Modifier,
    leadingIcon: Any?,
    enable: Boolean = true,
    placeHolder: String,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = false,
    imeAction: ImeAction,
    errorMessage: List<String> = emptyList(),
    keyboardActions: KeyboardActions = KeyboardActions(),
    keyboardType: KeyboardType = KeyboardType.Text,
) {

    var transformPassword by remember {
        mutableStateOf(keyboardType == KeyboardType.Password)
    }

    var leadingIconSize by remember {
        mutableStateOf(0f)
    }

    Column(
        modifier = modifier
            .padding(horizontal = smallPadding)
            .fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                if (leadingIcon is DrawableResource) {
                    Icon(
                        modifier = Modifier
                            .sizeIn(maxWidth = 24.dp, maxHeight = 24.dp),
                        painter = painterResource(resource = leadingIcon),
                        contentDescription = "Leading Icon",
                        tint = grey,
                    )
                } else if (leadingIcon is String) {
                    Text(
                        modifier = Modifier,
                        text = leadingIcon,
                        color = grey,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Start,
                        lineHeight = 30.sp,
                        fontFamily = NunitoFontFamily(),
                        fontWeight = FontWeight.Bold
                    )

                }
            }
            Spacer(Modifier.width(mediumPadding))

            BasicTextField(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                value = value,
                onValueChange = onValueChange,
                decorationBox = { innerTextField ->

                    if (value.isEmpty()) {
                        Text(
                            text = placeHolder,
                            color = grey,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Start,
                            lineHeight = 30.sp,
                            fontFamily = NunitoFontFamily(),
                            fontWeight = FontWeight.Medium
                        )
                    }
                    innerTextField.invoke()
                },
//                decorationBox = {
//
//                },
                textStyle = TextStyle(
                    color = black,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    lineHeight = 30.sp,
                    fontFamily = NunitoFontFamily(),
                    fontWeight = FontWeight.SemiBold
                ),
                enabled = enable,
                singleLine = singleLine,
                keyboardOptions = KeyboardOptions(
                    imeAction = imeAction,
                    keyboardType = keyboardType,
                    showKeyboardOnFocus = false
                ),
                keyboardActions = keyboardActions,
                visualTransformation = if (keyboardType == KeyboardType.Password && transformPassword && value.isNotBlank()) PasswordVisualTransformation() else VisualTransformation.None
            )



            if (keyboardType == KeyboardType.Password) {
                val eyeIcon =
                    if (transformPassword) Res.drawable.eye else Res.drawable.open_eye
                Icon(
                    modifier = Modifier
                        .size(width = 25.dp, height = 20.dp)
                        .clickable {
                            transformPassword = !transformPassword
                        },
                    painter = painterResource(eyeIcon),
                    contentDescription = "Password",
                    tint = black,

                    )
            }


        }

        Column(
            modifier = Modifier
                .offset(x = largePadding)
                .padding(start = 7.dp, end = mediumPadding, top = 5.dp)
                //.background(errorColor)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {

            HorizontalDivider(
                modifier = Modifier
                    //  .padding(start = smallPadding)
                    .fillMaxWidth(),
                color = if (errorMessage.isEmpty()) grey else errorColor
            )

            Spacer(Modifier.height(smallPadding))

            AnimatedVisibility(
                modifier = Modifier,
                visible = errorMessage.isNotEmpty()
            ) {
                Column {
                    repeat(errorMessage.size) {
                        ErrorText(error = errorMessage[it])
                    }
                }

            }
        }
    }


}

@Composable
private fun ErrorText(
    error: String
) {
    Row(
        modifier = Modifier
            .padding(bottom = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(errorColor)
                .size(4.dp)
        )
        Text(
            modifier = Modifier,
            text = error,
            textAlign = TextAlign.Left,
            style = TextStyle(
                lineHeight = 30.sp,
                fontFamily = NunitoFontFamily(),
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = errorColor
            )
        )
    }

}