using extOSC;
using UnityEngine;
using UnityEngine.SceneManagement;

public class TouchOSCReceiver : MonoBehaviour
{
    private OSCReceiver receiver;

    private Vector2 _movement;
    private Vector2 _camera;

    private bool _isRunning = false;
    private bool _isLeftBlocking = false;
    private bool _isRightBlocking = false;
    private bool _isCreating = false;
    private bool _isJumping = false;
    private bool _isHitting = false;

    [SerializeField]
    private bool change;

    // Start is called once before the first execution of Update after the MonoBehaviour is created
    void Start()
    {
        receiver = gameObject.AddComponent<OSCReceiver>();
        receiver.LocalPort = 7001;
        receiver.Bind("/move", UpdateMovement); // Movement
        receiver.Bind("/see", UpdateCamera);

        receiver.Bind("/button10", HandleChangeWorld); // World
        receiver.Bind("/button9", HandleRightBlock); // Right block
        receiver.Bind("/button8", HandleLeftBlock); // Left block
        receiver.Bind("/button7", HandleRun); // Correr toggle
        receiver.Bind("/button6", HandleExit); // Exit game
        receiver.Bind("/button5", HandleCreate); // Create block
        receiver.Bind("/button4", HandleJump); // Jump
        receiver.Bind("/button3", HandleHit); // Hit block

        _movement = Vector2.zero;
        _camera = Vector2.zero;
    }

    protected void HandleChangeWorld(OSCMessage message)
    {
        if (change)
        {
            SceneManager.LoadScene("Minecraft");
        }else
        {
            SceneManager.LoadScene("Agents");
        }
            
    }

    protected void UpdateMovement(OSCMessage message)
    {
        if (message.ToVector2(out Vector2 rawInput))
        {
            // Converte de [0,1] para [-1,1]
            _movement.x = (rawInput.x * 2) - 1;
            _movement.y = (rawInput.y * 2) - 1;
        }
    }

    protected void UpdateCamera(OSCMessage message)
    {
        if (message.ToVector2(out Vector2 rawInput))
        {
            _camera.x = (rawInput.x * 2) - 1;
            _camera.y = (rawInput.y * 2) - 1;
        }
    }

    protected void HandleRun(OSCMessage message)
    {
        _isRunning = message.Values[0].FloatValue == 1;
    }

    protected void HandleLeftBlock(OSCMessage message)
    {
        if (message.Values[0].FloatValue == 1)
            _isLeftBlocking = true;
    }

    protected void HandleRightBlock(OSCMessage message)
    {
        if (message.Values[0].FloatValue == 1)
            _isRightBlocking = true;
    }

    protected void HandleCreate(OSCMessage message)
    {
        if (message.Values[0].FloatValue == 1)
            _isCreating = true;
    }

    protected void HandleJump(OSCMessage message)
    {
        if(message.Values[0].FloatValue == 1)
            _isJumping = true;
    }

    protected void HandleHit(OSCMessage message)
    {
        if (message.Values[0].FloatValue == 1)
            _isHitting = true;

        if(change)
            SceneManager.LoadScene("PlayAgent");
    }

    protected void HandleExit(OSCMessage message)
    {
        if (message.Values[0].FloatValue == 1)
            Application.Quit();
    }

    public Vector2 GetMovement()
    {
        return _movement;
    }

    public Vector2 GetCamera()
    {
        return _camera;
    }

    public bool IsRunning()
    {
        return _isRunning;
    }

    public bool IsJumping()
    {
        bool aux = _isJumping;
        _isJumping = false;
        return aux;
    }

    public bool LeftBlock()
    {
        bool aux = _isLeftBlocking;
        _isLeftBlocking = false;
        return aux;
    }

    public bool RightBlock()
    {
        bool aux = _isRightBlocking;
        _isRightBlocking = false;
        return aux;
    }

    private bool CreateBlock()
    {
        bool aux = _isCreating;
        _isCreating = false;
        return aux;
    }
        
    public bool DestroyBlock()
    {
        bool aux = _isHitting;
        _isHitting = false;
        return aux;
    }

    public bool Interaction()
    {
        return _isHitting || CreateBlock();
    }
}
