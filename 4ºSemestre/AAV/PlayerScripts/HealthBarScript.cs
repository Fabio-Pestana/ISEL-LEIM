using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class HealthBarScript : MonoBehaviour
{
    public Image _bar;
    public Image _easeBar;
    public Player3P script;
    private float speedTransition = 0.05f;

    // Update is called once per frame
    void Update()
    {
        HealthChange(script.getHp());

        if (_bar.fillAmount != _easeBar.fillAmount)
        {
            EaseChange(script.getHp());
        }
    }


    void HealthChange(int health)
    {
        float amount = (health / 100.0f) * 180.0f / 360;
        _bar.fillAmount = amount;
    }

    void EaseChange(int health)
    {
        float amount = (health / 100.0f) * 180.0f / 360;
        _easeBar.fillAmount = Mathf.Lerp(_easeBar.fillAmount, amount, speedTransition);
    }
}
