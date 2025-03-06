import { IMessage } from 'capacitor-imessage';

window.testEcho = () => {
    const inputValue = document.getElementById("echoInput").value;
    IMessage.echo({ value: inputValue })
}
